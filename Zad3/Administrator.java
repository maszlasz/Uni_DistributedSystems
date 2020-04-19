import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Administrator {

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        
        Channel communicationChannel = connection.createChannel();
        Channel snoopingChannel = connection.createChannel();

        communicationChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);
        snoopingChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);

        String snoopingQueue = snoopingChannel.queueDeclare().getQueue();

        snoopingChannel.queueBind(snoopingQueue, "generalExchange", "#");

        Consumer snoopingConsumer = new DefaultConsumer(snoopingChannel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println("#SNOOPED: " + message);

                snoopingChannel.basicAck(envelope.getDeliveryTag(), false);
            }

        };

        snoopingChannel.basicConsume(snoopingQueue, false, snoopingConsumer);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            String routingKey;

            validate: while(true) {

                System.out.println("ENTER TYPE [AGENCIES | CARRIERS | ALL]:");

                switch(br.readLine().trim().toUpperCase()) {

                    case "AGENCIES":
                        routingKey = "AGENCY";
                        break validate;

                    case "CARRIERS":
                        routingKey = "CARRIER";
                        break validate;

                    case "ALL":
                        routingKey = "AGENCY.CARRIER";
                        break validate;

                    default:
                        System.out.println("WRONG");
                }

            }

            System.out.println("ENTER MESSAGE:");

            String message = br.readLine();
            message = "#ADMIN: " + message;

            communicationChannel.basicPublish("generalExchange", routingKey, null, message.getBytes(StandardCharsets.UTF_8));
        }
    }
}
