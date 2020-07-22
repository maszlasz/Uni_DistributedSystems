package utility;

import java.sql.Connection;
import java.util.UUID;

public class DBMsg {

    public UUID id;
    public String product;
    public int count;
    public Connection conn;

    public DBMsg(UUID id, String product, int count, Connection conn) {

        this.id = id;
        this.product = product;
        this.count = count;
        this.conn = conn;

    }

}
