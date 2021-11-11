
import com.rabbitmq.client.Channel;

import java.util.concurrent.TimeUnit;

public class Sensor extends Thread{

    private String queueName;

    private int delay;

    private Channel channel;

    private int id;

    public Sensor(String queueName, int delay, Channel channel, int id){
        this.queueName = queueName;
        this.delay = delay;
        this.channel = channel;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = String.valueOf(Math.random() * 40);
                channel.basicPublish("", queueName, null, message.getBytes());
                System.out.println(queueName + " sensor nr." + id + " sent: '" + message + "'");
                TimeUnit.SECONDS.sleep(delay);
            }
        } catch (Exception e){}
    }
}
