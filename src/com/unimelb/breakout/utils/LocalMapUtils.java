package com.unimelb.breakout.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.unimelb.breakout.R;

public class LocalMapUtils {
	
    /** The path to the maps file in the classpath. */
    private static final String MAPS_FILE = "./maps/maplist.json";


    public static String getMap(Context context){
//    	try{
//	        InputStream in = LocalMapUtils.class.getResourceAsStream(MAPS_FILE);
//	        byte[] b = new byte[in.available()];
//	        in.read(b);
//	        return new String(b);
//    	}catch(Exception e){
//    		e.printStackTrace();
//    		return "error";
//    	}
    	
    	String text = "";
    	try {
    		InputStream inputStream = context.getAssets().open("maps/maplist.json");
    		BufferedInputStream bis = new BufferedInputStream(inputStream);
    		
    		int fileLen = inputStream.available();
    		// Read the entire resource into a local byte buffer.
    		byte[] fileBuffer = new byte[fileLen];
    		inputStream.read(fileBuffer);
    		inputStream.close();
    		text = new String(fileBuffer);
    		
    	} catch (IOException e) {
    		text = "error";
    	}
    	
    	return text;
    	
//        try {
//            properties.load(in);
//        } catch (IOException e) {
//            Log.e("Failed to load properties file: {}", PROPERTIES_FILE, e);
//        } finally {
//            CloseableUtils.close(in);
//        }
    }

//    /**
//     * Load property from the project properties file.
//     * The properties may have arguments eg:
//     * <br>
//     * config.example={} World{}
//     * <br>
//     * get("config.example", "Hello", "!") => "Hello World!"
//     * @see MessageFormat
//     */
//    public static String get(String configOption, Object... args) {
//        if (properties.containsKey(configOption)) {
//            return FormatUtils.string(properties.getProperty(configOption), args);
//        } else {
//            throw new PropertyNotFoundException(
//                    FormatUtils.string("Property {} not found.", configOption));
//        }
//    }
}
