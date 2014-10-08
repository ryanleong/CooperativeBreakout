package com.unimelb.breakout.webservices;

import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.ScoreBoard;
import com.unimelb.breakout.object.UploadResponse;
import com.unimelb.breakout.utils.AsyncUtils;

/**
 * 
 * This class enables data communication with server.
 * 
 * @author Siyuan Zhang
 *
 */
public class DataManager {

	/**
	 * Download a list of all available maps from the server.
	 * 
	 * @param map
	 * @return
	 */
	public static ListenableFuture<MapList> getMapList() {
		
        return AsyncUtils.run(new Callable<MapList>() {
            @Override
            public MapList call() throws Exception {
            	
                return WebServices.getMapList();
            }
        });
    }
	
	/**
	 * Download a map from the server.
	 * 
	 * @param map
	 * @return
	 */
	public static ListenableFuture<Map> downloadNewMap(final String map) {
		
        return AsyncUtils.run(new Callable<Map>() {
            @Override
            public Map call() throws Exception {
            	
                return WebServices.downloadNewMap(map);
            }
        });
    }
	
	/**
	 * Get the score board from the server.
	 * 
	 * @return
	 */
	public static ListenableFuture<ScoreBoard> getScoreBoard() {
		
        return AsyncUtils.run(new Callable<ScoreBoard>() {
            @Override
            public ScoreBoard call() throws Exception {
            	
                return WebServices.getScoreBoard();
            }
        });
    }
	
	/**
	 * Upload a new high score to the server.
	 * 
	 * @param name
	 * @param score
	 * @return
	 */
	public static ListenableFuture<UploadResponse> uploadNewScore(final String name, final String score) {
		
        return AsyncUtils.run(new Callable<UploadResponse>() {
            @Override
            public UploadResponse call() throws Exception {
            	
                return WebServices.uploadNewScore(name, score);
            }
        });
    }
}
