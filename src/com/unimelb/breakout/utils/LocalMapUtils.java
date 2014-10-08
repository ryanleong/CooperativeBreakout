package com.unimelb.breakout.utils;

import java.io.IOException;
import java.io.InputStream;

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
    	Map map = null;
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
    		Log.e("FILE SYSTEM", "Error occurs when trying to open the map file. Map: " + name);
    	}
    	
    	return map;
    }
    
    public static String getNextLevel(String current, Context context){
    	
    	MapList maps = getMaps(context);
    	for(MapMeta map : maps.getMaps()){
    		if(map.getName().equals(current)){
    			String nextLevel = map.getNext();
    			if(hasMap(nextLevel, maps)){
        			return nextLevel;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public static boolean hasMap(String name, MapList maps){
    	
    	for(MapMeta map : maps.getMaps()){
    		if(map.getName().equals(name)){
    			return true;
    		}
    	}
    	
    	return false;
    }
}
