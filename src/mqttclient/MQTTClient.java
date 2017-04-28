package mqttclient;

import static org.fusesource.hawtbuf.Buffer.utf8;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;



public class MQTTClient {
    
    private BlockingConnection connection;
    private SubscribeT subscribed;
    
    public void connect(String h){
        String host = "tcp://" + h + ":1883";
        try{
            MQTT mqtt = new MQTT();
            mqtt.setHost(host);
            connection = mqtt.blockingConnection();
            connection.connect();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public void subscribe(String topic){
        Topic[] topics = {new Topic(utf8(topic), QoS.AT_LEAST_ONCE)};
        try{
            connection.subscribe(topics);
            SubscribeT subscribe = new SubscribeT(connection);
            subscribe.start();
            subscribed = subscribe;
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void publish(String topic, String message){
        try{
            connection.publish(topic, message.getBytes(), QoS.AT_LEAST_ONCE, false);
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
    
    public void unsuscribe(){
        subscribed.stopThread();
    }

    public void disconnect(){
        try {
            subscribed.stopThread();
            connection.disconnect();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}