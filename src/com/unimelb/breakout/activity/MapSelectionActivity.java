package com.unimelb.breakout.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonSyntaxException;
import com.unimelb.breakout.R;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.MapMeta;
import com.unimelb.breakout.utils.AsyncUtils;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.webservice.DataManager;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Player can choose a game level to play in this activity.
 * 
 * @author Siyuan
 *
 */
public class MapSelectionActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapselection);
		
		//Bundle bundle = this.getIntent().getExtras();

		MapList mapList;
		try {
			mapList = LocalMapUtils.getMaps(this);
			addGameButtons(mapList);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showOkDialog(this, "File Not Found", "Cannot find the map list file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.showOkDialog(this, "File Exception", "Cannot open the map list file");
		}
	}
	

	/**
	 * Load all game levels
	 * @param mapList
	 */
	public void addGameButtons(MapList mapList){
		for(final MapMeta map: mapList.getMaps()){
			
			final String name = map.getName();
			LinearLayout layout = (LinearLayout) this.findViewById(R.id.maps_collection);
			
            final boolean hasDownloaded = LocalMapUtils.hasDownloaded(name, MapSelectionActivity.this);

            Button button = null;
            if(map.getType().equals("remote") && !hasDownloaded){
                button = (Button) getLayoutInflater().inflate(R.layout.button_mapselection_undownload, layout, false);
            }else{
                button = (Button) getLayoutInflater().inflate(R.layout.button_mapselection, layout, false);
            }

            button.setTag(name);
            button.setText(name);
            
            
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	
                    boolean requireDownload = false;
                    if(map.getType().equals("remote")){
                		//if the selected one is a remote map
                    	if(!hasDownloaded){
                    		//download the map
                    		requireDownload = true;
                    	}

                	}
                	
                	if(requireDownload){
                		showDownloadDialog(map);

                	}else{
                		Utils.chooseScreenOrientation(MapSelectionActivity.this, name);
                	}
                	
                }
            });
            
            layout.addView(button);
		}	
	}
	
	public void showDownloadDialog(final MapMeta map){
		
		final Dialog dialog = new Dialog(this, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_downloadmap);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_downloadmap_title);
        TextView detail = (TextView) dialog.findViewById(R.id.dialog_downloadmap_detail);

        
        Button download = (Button) dialog.findViewById(R.id.dialog_download_button);
        Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel_button);

        title.setText("Download");

        detail.setText("Do you want to download this map?");
    
        download.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downloadNewMap(map);
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog.dismiss();     	
            }
        });
        
        dialog.show();
	}
	
	/**
	 * Download a new level from the server.
	 * @param m
	 */
	public void downloadNewMap(MapMeta m){
		final Dialog loadingDialog = Utils.showLoadingDialog(this, "Downloading the selected map..");
		final ListenableFuture<Map> map = DataManager.downloadNewMap(m.getName());
        AsyncUtils.addCallback(map, new FutureCallback<Map>() {
            @Override
            public void onSuccess(Map m) {
            	Log.d("MAPLISTFUTURE", "Download succeeds");
            	try {
					LocalMapUtils.saveDownloadedMap(m, MapSelectionActivity.this);
	        		Utils.showOkDialog(MapSelectionActivity.this, "Congratulations!", "The map you choose has been downloaded.");          
	                recreate();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
	        		Utils.showOkDialog(MapSelectionActivity.this, "File Exception", "The downloaded map cannot be saved");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
	        		Utils.showOkDialog(MapSelectionActivity.this, "IO Exception", "Unknown error when saving map.");
					e.printStackTrace();
				}
        		loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable throwable) {
            	
            	if(throwable instanceof SocketException){
                	Utils.showOkDialog(MapSelectionActivity.this, 
                			"Download Failed", 
                			"Fail to build connection. Please try it later. ");
                    Log.e("MAPLISTFUTURE", "Throwable during getmaplist:" + throwable);
            	}else if(throwable instanceof SocketTimeoutException){
            		Utils.showOkDialog(MapSelectionActivity.this, 
                			"Socket Timeout", 
                			"Connection is timeout. Please try it late.");
            	}else if(throwable instanceof JsonSyntaxException){
            		Utils.showOkDialog(MapSelectionActivity.this, 
                			"Download Failed", 
                			"Unexpected response from the server. Please try it later. ");
            	}else{
            		Utils.showOkDialog(MapSelectionActivity.this, 
                			"Download Failed", 
                			"Unknown Error. Please try it later. ");
                    Log.e("MAPLISTFUTURE", "Throwable during getmaplist:" + throwable);
            	}
            	loadingDialog.dismiss();
            }
        });
	}
	
	@Override
	public void onBackPressed() {       
	    finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);        
	}
}
