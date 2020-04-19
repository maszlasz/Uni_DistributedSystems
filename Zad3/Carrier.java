import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Carrier {

    public static void main(String[] argv) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] specializationWords;
        
        while(true) {

            System.out.println("ENTER TWO DIFFERENT SPECIALIZATIONS [PEOPLE | GOODS | SATELLITE]:");
            String specialization = br.readLine().toUpperCase();
            specializationWords = specialization.trim().toUpperCase().split("\\s+");

            if(specializationWords.length == 2) {
                
                if(specializationWords[0].matches("PEOPLE|GOODS|SATELLITE") &&
                   specializationWords[1].matches("PEOPLE|GOODS|SATELLITE") &&
                   ! specializationWords[0].equals(specializationWords[1])) {

                    break;

                } else {
                    
                    System.out.println("WRONG SPECIALIZATIONS");
                    
                }
            } else {
                
                System.out.println("WRONG NUMBER OF SPECIALIZATIONS");
                
            }
        }

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        
        Channel specialization1Channel = connection.createChannel();
        Channel specialization2Channel = connection.createChannel();
        Channel communicationChannel = connection.createChannel();
        Channel ordersReturnChannel = connection.createChannel();
        
        specialization1Channel.basicQos(1);
        specialization2Channel.basicQos(1);

        specialization1Channel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);
        specialization2Channel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);
        communicationChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);
        ordersReturnChannel.exchangeDeclare("generalExchange", BuiltinExchangeType.TOPIC);


        String specialization1Queue = specializationWords[0];
        specialization1Channel.queueDeclare(specialization1Queue, false, false, true, null);
        specialization1Channel.queueBind(specialization1Queue, "generalExchange", specializationWords[0]);

        String specialization2Queue = specializationWords[1];
        specialization2Channel.queueDeclare(specialization2Queue, false, false, true, null);
        specialization2Channel.queueBind(specialization2Queue, "generalExchange", specializationWords[1]);

        String communicationQueue = communicationChannel.queueDeclare().getQueue();
        communicationChannel.queueBind(communicationQueue, "generalExchange", "#.CARRIER.#");

        Consumer specialization1Consumer = new DefaultConsumer(specialization1Channel) {
            
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println(message);

                String agencyRoutingKey = message.substring(message.indexOf(" ") + 1, message.lastIndexOf("["));
                String messageReturn = "#DONE: " + message.substring(message.indexOf(" ") + 1);

                ordersReturnChannel.basicPublish("generalExchange", agencyRoutingKey,
                        null, messageReturn.getBytes(StandardCharsets.UTF_8));

                specialization1Channel.basicAck(envelope.getDeliveryTag(), false);
                
            }
            
        };

        Consumer specialization2Consumer = new DefaultConsumer(specialization2Channel) {
            
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println(message);
                
                String agencyRoutingKey = message.substring(message.indexOf(" ") + 1, message.lastIndexOf("["));
                String messageReturn = "#DONE: " + message.substring(message.indexOf(" ") + 1);

                ordersReturnChannel.basicPublish("generalExchange", agencyRoutingKey,
                        null, messageReturn.getBytes(StandardCharsets.UTF_8));

                specialization2Channel.basicAck(envelope.getDeliveryTag(), false);
                
            }
            
        };

        Consumer communicationConsumer = new DefaultConsumer(communicationChannel) {
            
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                
                String message = new String(body, StandardCharsets.UTF_8);

                System.out.println(message);

                communicationChannel.basicAck(envelope.getDeliveryTag(), false);
            }
            
        };


        specialization1Channel.basicConsume(specialization1Queue, false, specialization1Consumer);
        specialization2Channel.basicConsume(specialization2Queue, false, specialization2Consumer);
        communicationChannel.basicConsume(communicationQueue, false, communicationConsumer);


        System.out.println("UP AND RUNNING");

    }

}
