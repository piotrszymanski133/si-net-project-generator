
import com.rabbitmq.client.Channel;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class Sensor extends Thread{

    private String queueName;

    private int delay;

    private Channel channel;

    private int id;

    private int rangeFrom;

    private int rangeTo;

    public Sensor(String queueName, int delay, Channel channel, int id, int rangeFrom, int rangeTo){
        this.queueName = queueName;
        this.delay = delay;
        this.channel = channel;
        this.id = id;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
    }

    @Override
    public void run() {
        try {
            while (true) {
                double value = Math.random() * (rangeTo - rangeFrom) + rangeFrom;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                JSONObject jo = new JSONObject();
                jo.put("id", queueName + id);
                jo.put("value", value);
                jo.put("timestamp", timestamp.getTime());


                channel.basicPublish("", queueName, null, jo.toString().getBytes());
                System.out.println(queueName + " sensor nr." + id + " sent: '" + jo.toString() + "'");
                TimeUnit.SECONDS.sleep(delay);
            }
        } catch (Exception e){}
    }
}
