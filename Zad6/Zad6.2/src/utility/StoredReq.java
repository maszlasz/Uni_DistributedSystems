package utility;

import akka.actor.ActorRef;

import java.util.LinkedList;
import java.util.List;

public class StoredReq {

    public ActorRef ref;
    public String product;
    public int count;
    public List<Integer> prices;

    public StoredReq(ActorRef ref, String product) {

        this.ref = ref;
        this.product = product;
        this.prices = new LinkedList<>();
        this.count = 0;

    }

    public void add(int price) {

        prices.add(price);

    }

    public int size() {

        return prices.size();

    }

    public int getBest() {

        if(prices.size() == 2) {

            return prices.get(0) <= prices.get(1) ? prices.get(0) : prices.get(1);

        } else if (prices.size() == 1) {

            return prices.get(0);

        }

        return 0;

    }

}
