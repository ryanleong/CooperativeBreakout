package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.object.MapList;
import com.unimelb.breakout.preference.AccountPreference;
import com.unimelb.breakout.utils.JsonUtils;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.utils.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setButtonListner();
        if(!AccountPreference.hasPlayerName()){
            Utils.showPlayerName(this, "What's your name?");
        }      
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void setButtonListner(){
    	final Button spButton  = (Button) this.findViewById(R.id.button_single_player);
    	final Button mpButton  = (Button) this.findViewById(R.id.button_multi_player);
    	final Button scoreButton  = (Button) this.findViewById(R.id.button_score);
    	final Button quitButton  = (Button) this.findViewById(R.id.button_quit);

    	spButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


				try{           
					Log.d("TOUCH", "start modeSelectionActivity");
					final Intent intent = new Intent(WelcomeActivity.this, ModeSelectionActivity.class);
					//intent.putExtra("map_type", "local");
					startActivity(intent); 
			        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);        
			    } catch(Exception ex) {
			    	ex.printStackTrace();
			    }
			}
    		
    	});
    	
    	mpButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
    		
    	});
    	
    	scoreButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				
				try{           
					Log.d("TOUCH", "start scoreActivity");
					// TODO Auto-generated method stub
					Intent intent = new Intent(WelcomeActivity.this, ScoreBoardActivity.class);
					startActivity(intent);
			        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);        
			    } catch(Exception ex) {
			    	ex.printStackTrace();
			    }
			}
    		
    	});

    	  	
    	quitButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//quit the game
				finish();
			}
    		
    	});
    }
}
