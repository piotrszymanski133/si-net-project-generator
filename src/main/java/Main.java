import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeUnit;

public class Main {
    private final static String FIRST_QUEUE_NAME = "temperature";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(FIRST_QUEUE_NAME, false, false, false, null);
            while (true) {
                String message = String.valueOf(Math.random() * 40);
                channel.basicPublish("", FIRST_QUEUE_NAME, null, message.getBytes());
                System.out.println("Sent: '" + message + "'");
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred.");
        }
    }

}
