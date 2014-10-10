package com.unimelb.breakout.webservices;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

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
	public static int SERVER_PORT = 9876;

    
	/**
	 * Use UDP protocol to build a connection with the server and 
	 * request a list of available maps.
	 * 
	 * @param map
	 * @return
	 */
	public static MapList getMapList(){
		
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("request_type", "get_map_list");
		
		String json = JsonUtils.toJson(hashmap);
		
		DatagramSocket aSocket = null;
	    try {
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
	      aSocket.receive(reply);
	      
	      String newMap = new String(reply.getData());
	      MapList maps = JsonUtils.fromJson(newMap.trim(), MapList.class);

	      return maps;
	      
	    }catch (SocketException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }catch (IOException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }catch(Exception e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }finally {
	    	if(aSocket != null) 
	    		aSocket.close();
	    }
	    
	    return null;
	}
	
	/**
	 * Use UDP protocol to build a connection with the server and 
	 * request a new map.
	 * 
	 * @param map
	 * @return
	 */
	public static Map downloadNewMap(String map){
		
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("request_type", "get_map");
		hashmap.put("map_name", map);
		
		String json = JsonUtils.toJson(hashmap);
		
		DatagramSocket aSocket = null;
	    try {
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
	      aSocket.receive(reply);
	      
	      String newMap = new String(reply.getData());
	      return JsonUtils.fromJson(newMap.trim(), Map.class);

	    }catch (SocketException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }catch (IOException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }finally {
	    	if(aSocket != null) 
	    		aSocket.close();
	    }
	    
	    return null;
	}

	/**
	 * Use UDP protocol to build connection with the server and 
	 * get the score board from it.
	 * 
	 * @return
	 */
	public static ScoreBoard getScoreBoard(){
		
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("request_type", "get_score");
		
		String json = JsonUtils.toJson(hashmap);
		
		DatagramSocket aSocket = null;
	    try {
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
	      aSocket.receive(reply);
	      
	      String scoreboard = new String(reply.getData());
	      
	      return JsonUtils.fromJson(scoreboard.trim(), ScoreBoard.class);

	    }catch (SocketException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }catch (IOException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }finally {
	    	if(aSocket != null) 
	    		aSocket.close();
	    }
	    
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
	public static UploadResponse uploadNewScore(String name, String score){
		
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("request_type", "write_score");
		hashmap.put("player_name", name);
		hashmap.put("score", score);
		
		String json = JsonUtils.toJson(hashmap);
		
		DatagramSocket aSocket = null;
	    try {
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
	      aSocket.receive(reply);
	      
	      String response = new String(reply.getData());
	      
	      return JsonUtils.fromJson(response.trim(), UploadResponse.class);

	    }catch (SocketException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }catch (IOException e){
	    	Log.e("WebService", "socket: " + e.getMessage());
	    }finally {
	    	if(aSocket != null) 
	    		aSocket.close();
	    }
	    
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
