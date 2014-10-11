package com.unimelb.breakout.webservices;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.ScoreBoard;
import com.unimelb.breakout.object.UploadResponse;
import com.unimelb.breakout.utils.JsonUtils;

/**
 * This class now uses UDP connection with the server. 
 * If the server supports HTTP connection, then the 
 * HTTP Manager can be used. 
 * 
 * @author Siyuan Zhang
 *
 */
public class WebServices {
	
	public static String SERVER_IP = "192.168.0.3";
	public static int SERVER_PORT = 6789;
	
	public final static int timeout = 500;

    
	/**
	 * Use UDP protocol to build a connection with the server and 
	 * request a list of available maps.
	 * 
	 * @param map
	 * @return
	 */
	public static MapList getMapList() 
			throws SocketException, SocketTimeoutException, IOException, JsonSyntaxException{
		
		JSONObject object = new JSONObject();
		
		try {
			object.put("request_type", "get_map_list");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String json = object.toString();
		
		DatagramSocket aSocket = null;
		
	    aSocket = new DatagramSocket();
	    byte [] m = json.getBytes();
	    InetAddress aHost = InetAddress.getByName(SERVER_IP);
	    DatagramPacket request =
	    new DatagramPacket(m,  json.length(), aHost, SERVER_PORT);
	    aSocket.send(request);
	    Log.d("WebService", "Requesting for map list from server");
	    byte[] buffer = new byte[1000];
	      
	    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	    Log.d("WebService", "Waiting for map list from server");
	      
	    aSocket.setSoTimeout(timeout);
	    aSocket.receive(reply);
	      
	    String newMap = new String(reply.getData());

	    if(aSocket != null) 
	    	aSocket.close();
			
	    MapList maps = JsonUtils.fromJson(newMap.trim(), MapList.class);
			  
	    if(maps != null)
	    	return maps;

		return null;
	}
	
	/**
	 * Use UDP protocol to build a connection with the server and 
	 * request a new map.
	 * 
	 * @param map
	 * @return
	 */
	public static Map downloadNewMap(String map) 
			throws SocketException, SocketTimeoutException, IOException, JsonSyntaxException {
		
		JSONObject object = new JSONObject();
		
		try {
			object.put("request_type", "get_map");
			object.put("map_name", map);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String json = object.toString();
		
		DatagramSocket aSocket = null;

		aSocket = new DatagramSocket();
		byte [] m = json.getBytes();
		InetAddress aHost = InetAddress.getByName(SERVER_IP);
		DatagramPacket request =
				new DatagramPacket(m,  json.length(), aHost, SERVER_PORT);
		aSocket.send(request);
		Log.d("WebService", "Requesting for new map from server");
		byte[] buffer = new byte[1000];
  
		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
		Log.d("WebService", "Waiting for new map from server");
  
		aSocket.setSoTimeout(timeout);
		aSocket.receive(reply);
  
		String newMap = new String(reply.getData());

		if(aSocket != null) 
	    	aSocket.close();
		
		Map mMap =  JsonUtils.fromJson(newMap.trim(), Map.class);
		  
		if(mMap != null)
			return mMap;

	    return null;
	}

	/**
	 * Use UDP protocol to build connection with the server and 
	 * get the score board from it.
	 * 
	 * @return
	 */
	public static ScoreBoard getScoreBoard() 
			throws SocketException, SocketTimeoutException, IOException, JsonSyntaxException{
		
		JSONObject object = new JSONObject();
		
		try {
			object.put("request_type", "get_score");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String json = object.toString();
		
		DatagramSocket aSocket = null;

		aSocket = new DatagramSocket();
		byte [] m = json.getBytes();
		InetAddress aHost = InetAddress.getByName(SERVER_IP);
		DatagramPacket request =
		new DatagramPacket(m,  json.length(), aHost, SERVER_PORT);
		aSocket.send(request);
	    Log.d("WebService", "Requesting for score board from server");
	    byte[] buffer = new byte[1000];
		  
	    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	    Log.d("WebService", "Waiting for score board from server");
		  
	    aSocket.setSoTimeout(timeout);
	    aSocket.receive(reply);
		  
	    String scoreboard = new String(reply.getData());
		  
		
	    if(aSocket != null) 
	    	aSocket.close();
		
		ScoreBoard sboard =  JsonUtils.fromJson(scoreboard.trim(), ScoreBoard.class);
		  
		if(sboard != null)
			return sboard;

	    return null;
	}
	
	/**
	 * Use UDP protocol to build connection with server and 
	 * upload the new high score to it.
	 * 
	 * @param name
	 * @param score
	 * @return
	 */
	public static UploadResponse uploadNewScore(String name, int score)
			throws SocketException, SocketTimeoutException, IOException, JsonSyntaxException{
		
		JSONObject object = new JSONObject();
		
		try {
			object.put("request_type", "write_score");
			object.put("name", name);
			object.put("score", score);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String json = object.toString();
		
		DatagramSocket aSocket = null;

	    aSocket = new DatagramSocket();
	    byte [] m = json.getBytes();
	    InetAddress aHost = InetAddress.getByName(SERVER_IP);
	    DatagramPacket request =
	    new DatagramPacket(m,  json.length(), aHost, SERVER_PORT);
	    aSocket.send(request);
	    Log.d("WebService", "Unloading new score to the server");
	    byte[] buffer = new byte[1000];
	      
	    DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
	    Log.d("WebService", "Waiting for upload result from server");
	      
	    aSocket.setSoTimeout(timeout);
	    aSocket.receive(reply);
	      
	    String response = new String(reply.getData());
	      
	    if(aSocket != null) 
	    	aSocket.close();
		
	    UploadResponse uploadResponse =  JsonUtils.fromJson(response.trim(), UploadResponse.class);
		  
		if(uploadResponse != null)
			return uploadResponse;
	    
	    return null;
	}
	
    
// TODO: uncomment the codes below to use HTTP webservices
//	
//	   /**
//  * Statically holds cookie data for the entire program.
//  */
//  private static final HttpManager HTTPManager= new HttpManager();
//	
//	
//	
//	
//	public static MapList getMapList(){
//		
//		List<NameValuePair> dataToUrlEncode = new ArrayList<NameValuePair>();
//        dataToUrlEncode.add(new BasicNameValuePair("request_type", "get_map_list"));
//
//        return HTTPManager.postJson(GET_MAPLIST_URI, dataToUrlEncode, MapList.class);
//	}
//	
//	public static Map downloadNewMap(String map){
//		
//		List<NameValuePair> dataToUrlEncode = new ArrayList<NameValuePair>();
//        dataToUrlEncode.add(new BasicNameValuePair("request_type", "get_map"));
//        dataToUrlEncode.add(new BasicNameValuePair("map_name", map));
//        
//        return HTTPManager.postJson(GET_MAP_URI, dataToUrlEncode, MapList.class);
//	}
//	
//	public static ScoreBoard getScoreBoard(){
//		
//		List<NameValuePair> dataToUrlEncode = new ArrayList<NameValuePair>();
//        dataToUrlEncode.add(new BasicNameValuePair("request_type", "get_score"));
//        
//        return HTTPManager.postJson(GET_SCORE_URI, dataToUrlEncode, MapList.class);
//	}
//	
//	public static UploadResponse uploadNewScore(String name, String score){
//		
//		List<NameValuePair> dataToUrlEncode = new ArrayList<NameValuePair>();
//        dataToUrlEncode.add(new BasicNameValuePair("request_type", "write_score"));
//        dataToUrlEncode.add(new BasicNameValuePair("player_name", name));
//        dataToUrlEncode.add(new BasicNameValuePair("score", score));
//        
//        return HTTPManager.postJson(UPLOAD_SCORE_URI, dataToUrlEncode, MapList.class);
//	}
	
}
