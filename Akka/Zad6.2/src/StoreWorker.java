import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import utility.PriceReq;
import utility.PriceRes;

import java.util.concurrent.ThreadLocalRandom;

public class StoreWorker extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(PriceReq.class, o -> {

                    Thread.sleep(ThreadLocalRandom.current().nextInt(100, 500 + 1));

                    getSender().tell(new PriceRes(o.id, o.product,
                            ThreadLocalRandom.current().nextInt(1, 10 + 1), 0), ActorRef.noSender());

                })
                .matchAny(o -> log.info("# UNKNOWN MESSAGE"))
                .build();

    }

}
