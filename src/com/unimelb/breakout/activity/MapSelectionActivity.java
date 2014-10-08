package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.object.MapMeta;
import com.unimelb.breakout.utils.LocalMapUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
		
		MapList mapList = LocalMapUtils.getMaps(this);
		for(MapMeta map: mapList.getMaps()){
			
			final String name = map.getName();
			LinearLayout layout = (LinearLayout) this.findViewById(R.id.maps_collection);
            Button button = (Button) getLayoutInflater().inflate(R.layout.button_mapselection, layout, false);

            button.setTag(name);
            button.setText(name);
            
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
	                chooseScreenOrientation(name);
                }
            });
            
            layout.addView(button);
		}		
	}
	
	public void chooseScreenOrientation(final String mapName){
    	
		final Dialog dialog = new Dialog(this, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_screenorientation);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_screenorientation_title);
        TextView detail = (TextView) dialog.findViewById(R.id.dialog_screenorientation_detail);
        
        Button portrait = (Button) dialog.findViewById(R.id.dialog_portrait_button);
        Button landscape = (Button) dialog.findViewById(R.id.dialog_landscape_button);

        title.setText("Choose the screen mode");

        detail.setText("Please choose the screen mode you would like to play with.");

		final Intent intent = new Intent(MapSelectionActivity.this, MainActivity.class);
		
        portrait.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                intent.putExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                intent.putExtra("map", mapName);
                startActivity(intent);
            }
        });

        landscape.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog.dismiss();
                intent.putExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                intent.putExtra("map", mapName);
                startActivity(intent);
            }
        });
        
        dialog.show();
    }
}
