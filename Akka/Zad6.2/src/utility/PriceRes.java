package utility;

import java.util.UUID;

public class PriceRes {

    public UUID id;
    public String product;
    public int price;
    public int count;

    public PriceRes(UUID id, String product, int price, int count) {

        this.id = id;
        this.product = product;
        this.price = price;
        this.count = count;

    }

}
