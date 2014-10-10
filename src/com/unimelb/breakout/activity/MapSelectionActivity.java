package com.unimelb.breakout.activity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.unimelb.breakout.R;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.MapMeta;
import com.unimelb.breakout.utils.AsyncUtils;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.webservices.DataManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapSelectionActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapselection);
		
		//Bundle bundle = this.getIntent().getExtras();

		MapList mapList = LocalMapUtils.getMaps(this);
		addGameButtons(mapList);
	}
	

	
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

//	public void addMapDownloadButtons(MapList mapList){
//		for(final MapMeta map: mapList.getMaps()){
//			
//			final String name = map.getName();
//			LinearLayout layout = (LinearLayout) this.findViewById(R.id.maps_collection);
//            Button button = (Button) getLayoutInflater().inflate(R.layout.button_mapselection, layout, false);
//
//            button.setTag(name);
//            button.setText(name);
//            
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                	if(LocalMapUtils.hasDownloaded(name, MapSelectionActivity.this)){
//                		Utils.showOkDialog(MapSelectionActivity.this, "Downloaded", "The map you choose has already been downloaded.");
//                	}else{
//                    	downloadNewMap(map);
//                	}
//                }
//            });          
//            layout.addView(button);
//		}	
//	}
//	
	
	public void downloadNewMap(MapMeta m){
		final Dialog loadingDialog = Utils.showLoadingDialog(this, "Downloading the selected map..");
		final ListenableFuture<Map> map = DataManager.downloadNewMap(m.getName());
        AsyncUtils.addCallback(map, new FutureCallback<Map>() {
            @Override
            public void onSuccess(Map m) {
            	Log.d("MAPLISTFUTURE", "Download succeeds");
            	LocalMapUtils.saveDownloadedMap(m, MapSelectionActivity.this);
        		Utils.showOkDialog(MapSelectionActivity.this, "Congratulations!", "The map you choose has already been downloaded.");
                loadingDialog.dismiss();
                recreate();
            }

            @Override
            public void onFailure(Throwable throwable) {
            	loadingDialog.dismiss();
            	Utils.showOkDialog(MapSelectionActivity.this, 
            			"Downloading failed", 
            			"Sorry, download of new map failed. Please try it later. ");
                Log.e("MAPLISTFUTURE", "Throwable during getmaplist:" + throwable);
            }
        });
	}
	
	
	@Override
	public void onBackPressed() {       
	    finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);        
	}
}
