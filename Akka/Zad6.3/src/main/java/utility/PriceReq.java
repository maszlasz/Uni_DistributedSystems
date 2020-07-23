package utility;

import akka.actor.ActorRef;

import java.util.UUID;

public class PriceReq {

    public UUID id;
    public ActorRef ref;
    public String product;

    public PriceReq(ActorRef ref, String product) {

        this.id = UUID.randomUUID();
        this.ref = ref;
        this.product = product;

    }

}
