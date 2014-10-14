package com.unimelb.breakout.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.MapMeta;

/**
 * Utility class that defines read, write operations of map files
 * 
 * @author Siyuan Zhang
 *
 */
public class LocalMapUtils {
	
	private static final String MAPS_PATH = "maps/";
    private static final String MAPS_FILE = MAPS_PATH + "maplist";
    
    public static MapList getMaps(Context context) throws FileNotFoundException, IOException{
    	
		InputStream inputStream = context.getAssets().open(MAPS_FILE);
		
		int fileLen = inputStream.available();
		// Read the entire resource into a local byte buffer.
		byte[] fileBuffer = new byte[fileLen];
		inputStream.read(fileBuffer);
		inputStream.close();
		return JsonUtils.fromJson(new String(fileBuffer), MapList.class);
	
    }


    public static Map getMap(String name, Context context) throws FileNotFoundException, IOException{
    	MapList maps = getMaps(context);
    	MapMeta meta = findMapMeta(name, maps);
    	Map map = null;
    	if(meta.getType().equals("local")){

			InputStream inputStream = context.getAssets().open(MAPS_PATH+name);
			
			int fileLen = inputStream.available();
			// Read the entire resource into a local byte buffer.
			byte[] fileBuffer = new byte[fileLen];
			inputStream.read(fileBuffer);
			inputStream.close();
			String text = new String(fileBuffer);
			map = JsonUtils.fromJson(text, Map.class);

    	}else{
    	
			FileInputStream fis = context.openFileInput(name);
		    InputStreamReader isr = new InputStreamReader(fis);
		    BufferedReader bufferedReader = new BufferedReader(isr);
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = bufferedReader.readLine()) != null) {
		       sb.append(line);
		    }
		    
		    map = JsonUtils.fromJson(sb.toString().trim(), Map.class);
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
    
    public static MapMeta getNextLevel(String current, Context context) throws FileNotFoundException, IOException{
    	
    	MapList maps = getMaps(context);
    	MapMeta currentMap = findMapMeta(current, maps);
    	MapMeta nextMap = findMapMeta(currentMap.getNext(), maps);
    	
    	return nextMap;
    }

    
    public static boolean hasDownloaded(String name, Context context){
    	 File file = context.getFileStreamPath(name);
    	 return file.exists();
    }
    
    public static void saveDownloadedMap(Map map, Context context) throws FileNotFoundException, IOException{
    	
    	String filename = map.getName();
    	String string = JsonUtils.toJson(map);
    	FileOutputStream outputStream;

		outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
		outputStream.write(string.getBytes());
		outputStream.close();   	
    }

}
