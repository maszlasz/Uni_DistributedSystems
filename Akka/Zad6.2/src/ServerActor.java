import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import utility.*;

import scala.concurrent.duration.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.resume;

public class ServerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef store1;
    private ActorRef store2;

    Connection conn;

    private Map<UUID, StoredReq> storedReqs = new ConcurrentHashMap<>();

    private SupervisorStrategy strategy = new OneForOneStrategy(
        -1,
        Duration.create(10, TimeUnit.SECONDS),
        DeciderBuilder.matchAny(o -> resume()).build()
    );


    @Override
    public SupervisorStrategy supervisorStrategy() {

        return strategy;

    }

    @Override
    public void preStart() {

        store1 = context().actorOf(Props.create(StoreActor.class), "store1");
        store2 = context().actorOf(Props.create(StoreActor.class), "store2");

        try {

            conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            conn.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS requests (\n"
                            + "	name text PRIMARY KEY,\n"
                            + "	count integer NOT NULL\n"
                            + ");"
            );

        } catch (SQLException e) {

            System.out.println("# COULDN'T CONNECT TO THE DB. EXITING");
            System.exit(1);

        }

    }

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(PriceReq.class, o -> {

                    storedReqs.put(o.id, new StoredReq(o.ref, o.product));

                    getContext().system().scheduler().scheduleOnce(
                            Duration.create(300, TimeUnit.MILLISECONDS),
                            getSelf(),
                            new TimeoutMsg(o.id),
                            getContext().system().dispatcher(),
                            null
                    );

                    store1.tell(o, getSelf());
                    store2.tell(o, getSelf());

                    ActorRef dbWorker = getContext().actorOf(Props.create(DBWorker.class));
                    dbWorker.tell(new DBMsg(o.id, o.product, 1, conn), getSelf());
                    dbWorker.tell(PoisonPill.getInstance(), ActorRef.noSender());

                })
                .match(PriceRes.class, o -> {

                    if(storedReqs.containsKey(o.id)) {

                        storedReqs.get(o.id).add(o.price);

                        if(storedReqs.get(o.id).size() == 2) {

                            storedReqs.get(o.id).ref.tell(new PriceRes(null, storedReqs.get(o.id).product,
                                    storedReqs.get(o.id).getBest(), storedReqs.get(o.id).count), ActorRef.noSender());

                            storedReqs.remove(o.id);

                        }

                    }

                })
                .match(DBMsg.class, o -> {

                    if(storedReqs.containsKey(o.id)) {

                        storedReqs.get(o.id).count = o.count;

                    }

                })
                .match(TimeoutMsg.class, o -> {

                    if(storedReqs.containsKey(o.id)) {

//                        log.info("# TIMED OUT");

                        storedReqs.get(o.id).ref.tell(new PriceRes(null, storedReqs.get(o.id).product,
                                storedReqs.get(o.id).getBest(), storedReqs.get(o.id).count), ActorRef.noSender());

                        storedReqs.remove(o.id);


                    }

                })
                .matchAny(o -> log.info("# UNKNOWN MESSAGE"))
                .build();

    }

}
