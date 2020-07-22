package utility;

import java.util.UUID;

public class PriceRes {

    public UUID id;
    public String product;
    public int price;

    public PriceRes(UUID id, String product, int price) {

        this.id = id;
        this.product = product;
        this.price = price;

    }

}
