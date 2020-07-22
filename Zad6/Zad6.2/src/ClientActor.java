import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import utility.PriceReq;
import utility.PriceRes;

public class ClientActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(PriceReq.class, o -> {

                    o.ref.tell(new PriceReq(getSelf(), o.product), ActorRef.noSender());

                })
                .match(PriceRes.class, o -> {

                    if(o.price > 0) {

                        if(o.count > 0) {

                            log.info("# BEST PRICE FOR " + o.product + ": " + o.price + "; REQ COUNT: " + o.count);

                        } else {

                            log.info("# BEST PRICE FOR " + o.product + ": " + o.price);

                        }

                    }
                    else {

                        log.info("# NO PRICE FOUND FOR " + o.product);

                    }

                })
                .matchAny(o -> log.info("# UNKNOWN MESSAGE"))
                .build();

    }

}
