import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import utility.PriceReq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Runner {
    static ActorSystem system;
    static ActorRef server;

    public static void main(String [] args) {

        system = ActorSystem.create("system");

        final ActorRef client = system.actorOf(Props.create(ClientActor.class), "client");
        server = system.actorOf(Props.create(ServerActor.class), "server");

        initClients();

        System.out.println("# ENTER PRODUCT NAME TO GET PRICE OR '!q' TO QUIT OR '!f' TO CALL THE FUNCTION:");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {

            String line;

            try {

                line = br.readLine();
                if (line.equals("!q")) {

                    break;

                } else if (line.equals("!f")) {
                    multipleClients();
                } else {
                    client.tell(new PriceReq(server, line), null);
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        system.terminate();

    }



    static ActorRef client1;
    static ActorRef client2;
    static ActorRef client3;
    static ActorRef client4;

    private static void initClients() {
        client1 = system.actorOf(Props.create(ClientActor.class), "client1");
        client2 = system.actorOf(Props.create(ClientActor.class), "client2");
        client3 = system.actorOf(Props.create(ClientActor.class), "client3");
        client4 = system.actorOf(Props.create(ClientActor.class), "client4");
    }

    private static void multipleClients() {

        client1.tell(new PriceReq(server, "mleko"), null);
        client2.tell(new PriceReq(server, "mleko"), null);
        client3.tell(new PriceReq(server, "mleko"), null);
        client3.tell(new PriceReq(server, "chleb"), null);
        client3.tell(new PriceReq(server, "chleb"), null);
        client3.tell(new PriceReq(server, "chleb"), null);
        client4.tell(new PriceReq(server, "mleko"), null);

    }

}
