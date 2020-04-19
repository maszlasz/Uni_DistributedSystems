import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Agency {

    public static void main(String[] argv) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String agencyName;
        
        while(true) {
            
            System.out.println("ENTER AGENCY NAME:");
            agencyName = br.readLine().trim().toUpperCase();
            
            if(agencyName.isEmpty() || agencyName.equals("AGENCY")) {
                
                System.out.println("WRONG. AGENCY NAME CAN'T BE EMPTY OR \"AGENCY\"");
                
            } else {
                
                break;
            }
            
        }

        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        
        Channel ordersChannel = connection.createChannel();
        Channel communicationChannel = connection.createChannel();
        Channel ordersReturnChannel = connection.createChannel();
        
        ordersChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);
        communicationChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);
        ordersReturnChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);

        String communicationQueue = communicationChannel.queueDeclare().getQueue();
        communicationChannel.queueBind(communicationQueue, "generalExchange", "#.AGENCY.#");
        
        String ordersReturnQueue = ordersReturnChannel.queueDeclare().getQueue();
        ordersReturnChannel.queueBind(ordersReturnQueue, "generalExchange", agencyName);
        

        Consumer communicationConsumer = new DefaultConsumer(communicationChannel) {
            
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println(message);

                communicationChannel.basicAck(envelope.getDeliveryTag(), false);

            }
            
        };

        Consumer ordersReturnConsumer = new DefaultConsumer(ordersReturnChannel) {
            
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println(message);

                ordersReturnChannel.basicAck(envelope.getDeliveryTag(), false);

            }
            
        };

        communicationChannel.basicConsume(communicationQueue, false, communicationConsumer);
        ordersReturnChannel.basicConsume(ordersReturnQueue, false, ordersReturnConsumer);


        int orderCounter = 1;

        while(true) {

            String carrierRoutingKey,
                   orderType;

            while(true) {
                
                System.out.println("ENTER ORDER TYPE [PEOPLE | GOODS | SATELLITE]:");

                orderType = br.readLine().trim().toUpperCase();
                
                if(orderType.matches("PEOPLE|GOODS|SATELLITE")) {
                    
                    carrierRoutingKey = orderType ;
                    break;
                    
                }
                else {
                    
                    System.out.println("WRONG ORDER TYPE");
                    
                }
            }

            String order = "#ORDER: " + agencyName + "[" + orderCounter + "]" + ": " + orderType;

            ordersChannel.basicPublish("generalExchange", carrierRoutingKey, null, order.getBytes(StandardCharsets.UTF_8));

            orderCounter = orderCounter + 1;

        }

    }

}
