package mqttclient;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.Message;

public class SubscribeT implements Runnable{
    
    private BlockingConnection connection;
    public Thread hilo;
    private boolean stop = false;
    
    public SubscribeT(BlockingConnection c){
        connection = c;
    }
    
    public void stopThread(){
        stop = true;
        System.out.println("Subscribe Thread Finished");
    }
    
    public void start() {
	    System.out.println("Running Subscribe Thread");
	    if (hilo == null) {
	      hilo = new Thread (this);
	      hilo.start ();
	    }
    }
    
    @Override
    public void run(){
        while(!stop){
            try{
                Message message = connection.receive();
                MessageT m = new MessageT(new String(message.getPayload()));
                m.start();
                message.ack();
            }catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }
}
