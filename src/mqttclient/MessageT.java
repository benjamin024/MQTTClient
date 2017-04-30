package mqttclient;

public class MessageT implements Runnable{
	
	private String mensaje;
	private Thread hilo;
	
	public MessageT(String msg) {
		this.mensaje = msg;
	}
	
	public void start () {
	    if (hilo == null) {
	      hilo = new Thread (this, mensaje);
	      hilo.start ();
	    }
	 }
	
	private boolean terminar = false;
    public void setTerminar (boolean t)
    {
        this.terminar=t;
    }

    public void run(){
    	while(!terminar) {
	    	System.out.println("Running Message Thread");
	    	Json json = new Json();
	    	boolean ok = json.readJson(mensaje);
	
	    	if(ok)
	    		System.out.println("Message saved in data base");
	    	else
	    		System.out.println("Error saving message");
	    	System.out.println("Message Thread Finished");
	    	setTerminar(true);
    	}
    }
    
    public void setMensaje(String msj)
    {
        this.mensaje = msj;
    }
}