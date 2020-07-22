import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import utility.PriceReq;

public class StoreActor extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {

        return receiveBuilder()
                .match(PriceReq.class, o -> {

                    ActorRef storeWorker = getContext().actorOf(Props.create(StoreWorker.class));
                    storeWorker.tell(o, getSender());
                    storeWorker.tell(PoisonPill.getInstance(), ActorRef.noSender());

                })
                .matchAny(o -> log.info("# UNKNOWN MESSAGE"))
                .build();

    }

}
