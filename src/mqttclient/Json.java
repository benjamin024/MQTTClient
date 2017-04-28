package mqttclient;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Base64;

public class Json {
	
	private static String timeFormat(String time) throws Exception{
		time = time.replace('T', ' ');
		String t[] = time.split("\\.");
		time = t[0];
		
		return time;
	}
	
	private static final char[] DIGITS
    = {'0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	private static final String toHex(byte[] data) {
	final StringBuffer sb = new StringBuffer(data.length * 2);
	for (int i = 0; i < data.length; i++) {
	    sb.append(DIGITS[(data[i] >>> 4) & 0x0F]);
	    sb.append(DIGITS[data[i] & 0x0F]);
	}
	return sb.toString();
	}
	
	private static boolean saveMessage(String devEUI, String appID, String time, String frequency, String data){
		Conexion bd = new Conexion();
		if(bd.conectar()){
			String sql = "INSERT INTO mensaje(deveui, appid, mtime, frequency, data) VALUES('"+devEUI+"',"+appID+",'"+time+"','"+frequency+"','"+data+"');";
			if(bd.abc(sql)){
				bd.cerrar();
				return true;	
			}else{
				bd.cerrar();
				return false;
			}
		}else {
			bd.cerrar();
			return false;
		}
	}
	
	public boolean readJson(String json){
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(json);
			JSONObject jsonObject = (JSONObject) obj;
			String appID = (String) jsonObject.get("applicationID");
			String devEUI = (String) jsonObject.get("devEUI");
			JSONArray rxInfo = (JSONArray) jsonObject.get("rxInfo");
			JSONObject auxobj =  (JSONObject) rxInfo.get(0);
			String time = (String) auxobj.get("time");
			time = timeFormat(time);
			JSONObject txInfo = (JSONObject) jsonObject.get("txInfo");
			Long frequency =(Long) txInfo.get("frequency");
			String data = (String) jsonObject.get("data");
			
			byte[] decoded = Base64.getDecoder().decode(data);
			data = toHex(decoded);
			
			System.out.println("devEUI: "+devEUI);
			System.out.println("applicationID: "+appID);
			System.out.println("time: "+time);
			System.out.println("frequency: "+frequency);
			System.out.println("data: "+data);
			
			boolean saved = saveMessage(devEUI, appID, time, new String(""+frequency), data);
			
			return saved;
		}catch(Exception e){
			System.out.println(e.toString());
			return false;
		}
	}

}