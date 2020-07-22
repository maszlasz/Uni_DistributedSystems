import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import utility.DBMsg;

import java.sql.ResultSet;

public class DBWorker extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(DBMsg.class, o -> {

                    o.conn.createStatement().execute(
                            "INSERT OR IGNORE INTO requests (name, count) VALUES ('" + o.product + "', 0)"
                    );

                    ResultSet rs = o.conn.createStatement().executeQuery(
                            "SELECT count FROM requests WHERE name='" + o.product + "'"
                    );

                    getSender().tell(new DBMsg(o.id, o.product, rs.getInt("count")+1, null), ActorRef.noSender());

                    o.conn.createStatement().execute(
                            "UPDATE requests SET count = count + 1 WHERE name='" + o.product + "'"
                    );

                })
                .matchAny(o -> log.info("# UNKNOWN MESSAGE"))
                .build();

    }

}
