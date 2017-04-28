package mqttclient;

public class MessageT implements Runnable{
	
	private String mensaje;
	private Thread hilo;
	
	public MessageT(String msg) {
		this.mensaje = msg;
	}
	
	public void start () {
	    System.out.println("Mensaje: " + mensaje);
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
	    	System.out.println("Comienza el hilo");
	    	Json json = new Json();
	    	boolean ok = json.readJson(mensaje);
	
	    	if(ok)
	    		System.out.println("Mensaje guardado en la base de datos");
	    	else
	    		System.out.println("Error al guardar mensaje");
	    	System.out.println("Hilo terminado");
	    	setTerminar(true);
    	}
    }
    
    public void setMensaje(String msj)
    {
        this.mensaje = msj;
    }
}