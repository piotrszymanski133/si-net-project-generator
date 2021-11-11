
import com.rabbitmq.client.Channel;
import org.json.JSONObject;

import java.sql.Timestamp;
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
                double value = 0;
                if(queueName.equals("temperature")){
                    value = Math.random() * 40;
                } else if (queueName.equals("humidity")) {
                    value = Math.random() * 100;
                } else if (queueName.equals("wind")) {
                    value = Math.random() * 50;
                } else if (queueName.equals("pressure")) {
                    value = Math.random() * 2;
                }
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
