import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeUnit;

public class Main {
    private final static String FIRST_QUEUE_NAME = "temperature";
    private final static String SECOND_QUEUE_NAME = "humidity";
    private final static String THIRD_QUEUE_NAME = "wind";
    private final static String FOURTH_QUEUE_NAME = "pressure";

    private final static int FIRST_QUEUE_DELAY = 3;
    private final static int SECOND_QUEUE_DELAY = 4;
    private final static int THIRD_QUEUE_DELAY = 4;
    private final static int FOURTH_QUEUE_DELAY = 6;

    private final static int NUMBER_OF_SENSORS_PER_QUEUE = 10;

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(FIRST_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(FIRST_QUEUE_NAME, FIRST_QUEUE_DELAY, channel, i+1);
                sensor.start();
            }
            channel.queueDeclare(SECOND_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(SECOND_QUEUE_NAME, SECOND_QUEUE_DELAY, channel, i+1);
                sensor.start();
            }
            channel.queueDeclare(THIRD_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(THIRD_QUEUE_NAME, THIRD_QUEUE_DELAY, channel, i+1);
                sensor.start();
            }
            channel.queueDeclare(FOURTH_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(FOURTH_QUEUE_NAME, FOURTH_QUEUE_DELAY, channel, i+1);
                sensor.start();
            }
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred.");
        }
    }

}
