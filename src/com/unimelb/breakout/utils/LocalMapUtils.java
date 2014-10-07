package com.unimelb.breakout.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;

public class LocalMapUtils {
	
    /** The path to the maps file in the classpath. */
    private static final String MAPS_FILE = "./maps/maplist.json";
    
    public static MapList getMap(Context context){
    	try {
    		InputStream inputStream = context.getAssets().open("maps/maplist");
    		
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


    public Map getMap(String name, Context context){
    	Map map = null;
    	try {
    		InputStream inputStream = context.getAssets().open("maps/"+name);
    		BufferedInputStream bis = new BufferedInputStream(inputStream);
    		
    		int fileLen = inputStream.available();
    		// Read the entire resource into a local byte buffer.
    		byte[] fileBuffer = new byte[fileLen];
    		inputStream.read(fileBuffer);
    		inputStream.close();
    		String text = new String(fileBuffer);
    		map = JsonUtils.fromJson(text, Map.class);
    		
    	} catch (IOException e) {
    		Log.e("FILE SYSTEM", "Error occurs when trying to open the map file. Map: " + name);
    	}
    	
    	return map;
    }
    
//    public Map getNextLevel(Map current, MapList maps){
//    	Map next = null;
//    	for(MapMeta map : maps.getMaps()){
//    		if(map.getName().equals(current.getName())){
//    			next 
//    		}
//    	}
//    }
}
