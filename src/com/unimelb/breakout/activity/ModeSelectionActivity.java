package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Player can either play the challenge mode or the arcade mode.
 * 
 * @author Siyuan Zhang
 *
 */
public class ModeSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mode_selection);
		
    	final Button challenge  = (Button) this.findViewById(R.id.button_challenge);
    	final Button arcade  = (Button) this.findViewById(R.id.button_arcade);
    	
    	challenge.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        
				final Intent intent = new Intent(ModeSelectionActivity.this, MapSelectionActivity.class);
				//intent.putExtra("map_type", "local");
				startActivity(intent);
		        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);        

			}
    		
    	});
    	
    	arcade.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Utils.chooseScreenOrientation(ModeSelectionActivity.this, "1-0");      

			}
    		
    	});
    	
	}
	
	@Override
	public void onBackPressed() {       
	    finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);        
	}
}
