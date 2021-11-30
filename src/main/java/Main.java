import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeUnit;

public class Main {
    private final static String TEMPERATURE_QUEUE_NAME = "temperature";
    private final static String HUMIDITY_QUEUE_NAME = "humidity";
    private final static String WIND_QUEUE_NAME = "wind";
    private final static String PRESSURE_QUEUE_NAME = "pressure";

    private final static int NUMBER_OF_SENSORS_PER_QUEUE = 10;

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmq-SI-175557");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(TEMPERATURE_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(TEMPERATURE_QUEUE_NAME, "TEMPERATURE", channel, i+1);
                sensor.start();
            }
            channel.queueDeclare(HUMIDITY_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(HUMIDITY_QUEUE_NAME, "HUMIDITY", channel, i+1);
                sensor.start();
            }
            channel.queueDeclare(WIND_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(WIND_QUEUE_NAME, "WIND", channel, i+1);
                sensor.start();
            }
            channel.queueDeclare(PRESSURE_QUEUE_NAME, false, false, false, null);
            for (int i = 0; i < NUMBER_OF_SENSORS_PER_QUEUE; i++) {
                Sensor sensor = new Sensor(PRESSURE_QUEUE_NAME, "PRESSURE", channel, i+1);
                sensor.start();
                if(i == NUMBER_OF_SENSORS_PER_QUEUE-1){
                    sensor.join();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("An error occurred.");
        }
    }

}
