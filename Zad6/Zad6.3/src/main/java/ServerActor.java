import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import utility.PriceReq;
import utility.PriceRes;
import utility.StoredReq;
import utility.TimeoutMsg;

import scala.concurrent.duration.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.resume;

public class ServerActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private ActorRef store1;
    private ActorRef store2;

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

                })
                .match(PriceRes.class, o -> {

                    if(storedReqs.containsKey(o.id)) {

                        storedReqs.get(o.id).add(o.price);

                        if(storedReqs.get(o.id).size() == 2) {

                            storedReqs.get(o.id).ref.tell(new PriceRes(null, storedReqs.get(o.id).product,
                                    storedReqs.get(o.id).getBest()), ActorRef.noSender());

                            storedReqs.remove(o.id);

                        }

                    }

                })
                .match(TimeoutMsg.class, o -> {

                    if(storedReqs.containsKey(o.id)) {

                        //log.info("# TIMED OUT");

                        storedReqs.get(o.id).ref.tell(new PriceRes(null, storedReqs.get(o.id).product,
                                storedReqs.get(o.id).getBest()), ActorRef.noSender());

                        storedReqs.remove(o.id);
                    }

                })
                .matchAny(o -> log.info("# UNKNOWN MESSAGE"))
                .build();

    }

}
