
import com.rabbitmq.client.Channel;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class Sensor extends Thread{

    private final String queueName;

    private final Channel channel;

    private final String delayEnv;

    private final String rangeStartEnv;

    private final String rangeEndEnv;

    private final int id;


    public Sensor(String queueName, String type, Channel channel, int id){
        this.queueName = queueName;
        this.channel = channel;
        this.id = id;
        this.delayEnv = type + "QUEUE_DELAY";
        this.rangeStartEnv = type + "QUEUE_RANGE_FROM";
        this.rangeEndEnv = type + "QUEUE_RANGE_TO";

    }

    @Override
    public void run() {
        try {
            while (true) {
                double value = Math.random() * (getValueFromEnv(rangeEndEnv) - getValueFromEnv(rangeStartEnv)) + getValueFromEnv(rangeStartEnv);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                JSONObject jo = new JSONObject();
                jo.put("id", queueName + id);
                jo.put("value", value);
                jo.put("timestamp", timestamp.getTime());


                channel.basicPublish("", queueName, null, jo.toString().getBytes());
                System.out.println(queueName + " sensor nr." + id + " sent: '" + jo.toString() + "'");
                TimeUnit.SECONDS.sleep(getValueFromEnv(delayEnv));
            }
        } catch (Exception e){}
    }

    public int getValueFromEnv(String env) {
        return  Integer.parseInt(System.getenv(env));
    }
}
