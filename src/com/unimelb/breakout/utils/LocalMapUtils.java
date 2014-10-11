package com.unimelb.breakout.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.MapMeta;

public class LocalMapUtils {
	
	private static final String MAPS_PATH = "maps/";
    private static final String MAPS_FILE = MAPS_PATH + "maplist";
    
    public static MapList getMaps(Context context){
    	try {
    		InputStream inputStream = context.getAssets().open(MAPS_FILE);
    		
    		int fileLen = inputStream.available();
    		// Read the entire resource into a local byte buffer.
    		byte[] fileBuffer = new byte[fileLen];
    		inputStream.read(fileBuffer);
    		inputStream.close();
    		return JsonUtils.fromJson(new String(fileBuffer), MapList.class);
    		
    	} catch (IOException e) {
    		Log.e("FILE SYSTEM", "Error occurs when trying to open the map list file");
    		return null;
    	}
    }


    public static Map getMap(String name, Context context){
    	MapList maps = getMaps(context);
    	MapMeta meta = findMapMeta(name, maps);
    	Map map = null;
    	if(meta.getType().equals("local")){

	    	try {
	    		InputStream inputStream = context.getAssets().open(MAPS_PATH+name);
	    		
	    		int fileLen = inputStream.available();
	    		// Read the entire resource into a local byte buffer.
	    		byte[] fileBuffer = new byte[fileLen];
	    		inputStream.read(fileBuffer);
	    		inputStream.close();
	    		String text = new String(fileBuffer);
	    		map = JsonUtils.fromJson(text, Map.class);
	    		
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    		Log.e("FILE SYSTEM", "Error occurs when trying to open the map file. Map: " + name);
	    	}
    	}else{
    		
    		try{
	    		FileInputStream fis = context.openFileInput(name);
			    InputStreamReader isr = new InputStreamReader(fis);
			    BufferedReader bufferedReader = new BufferedReader(isr);
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = bufferedReader.readLine()) != null) {
			       sb.append(line);
			    }
			    
			    map = JsonUtils.fromJson(sb.toString().trim(), Map.class);
		    }catch(FileNotFoundException e){
		    	e.printStackTrace();
	    		Log.e("FILE SYSTEM", "Error occurs when trying to open the remote map file. Map: " + name);
		    } catch(IOException e){
		    	e.printStackTrace();
	    		Log.e("FILE SYSTEM", "Error occurs when trying to read the remote map file. Map: " + name);
		    }
    	}
    	return map;
    }
    
    public static MapMeta findMapMeta(String current, MapList maps){
    	if(current.equals("1-0")){
    		return new MapMeta(current, "local", current);
    	}else{
	    	for(MapMeta map : maps.getMaps()){
	    		if(map.getName().equals(current)){
	        		return map;
	    		}
	    	}  	
    	}
    	return null;
    }
    
    public static MapMeta getNextLevel(String current, Context context){
    	
    	MapList maps = getMaps(context);
    	MapMeta currentMap = findMapMeta(current, maps);
    	MapMeta nextMap = findMapMeta(currentMap.getNext(), maps);
    	
    	return nextMap;
    }
    
//    public static boolean hasMap(String name, Context context){
//    	MapList maps = getMaps(context);
//    	for(MapMeta map : maps.getMaps()){
//    		if(map.getName().equals(name)){
//    			return true;
//    		}
//    	}
//    	
//    	return hasDownloaded(name, context);
//    }
    
    public static boolean hasDownloaded(String name, Context context){
    	 File file = context.getFileStreamPath(name);
    	 return file.exists();
    }
    
    public static void saveDownloadedMap(Map map, Context context){
    	
    	String filename = map.getName();
    	String string = JsonUtils.toJson(map);
    	FileOutputStream outputStream;

    	try {
    	  outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
    	  outputStream.write(string.getBytes());
    	  outputStream.close();
    	} catch (FileNotFoundException e) {
    	  Log.e("Save downloaded map", "Failed to save the downloaded map. Failed to open.");
    	  e.printStackTrace();
    	} catch (IOException e){
    	  Log.e("Save downloaded map", "Failed to save the downloaded map. Failed to write.");
      	  e.printStackTrace();
    	}
    	
    }

}
