package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.preference.AccountPreference;
import com.unimelb.breakout.utils.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The welcome screen. Player can choose to start the game, view scores, or quit the game.
 * 
 * @author Siyuan Zhang
 *
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        setButtonListner();
        if(!AccountPreference.hasPlayerName()){
            Dialog dialog = Utils.showPlayerName(this, "Please have a name before you start.");
            dialog.setCancelable(false);
        }      
        
    }
    
    public void setButtonListner(){
    	final Button spButton  = (Button) this.findViewById(R.id.button_single_player);
    	final Button scoreButton  = (Button) this.findViewById(R.id.button_score);
    	final Button helpButton  = (Button) this.findViewById(R.id.button_help);
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
    	
    	helpButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				
				try{           
					Log.d("TOUCH", "start scoreActivity");
					// TODO Auto-generated method stub
					Intent intent = new Intent(WelcomeActivity.this, HelpActivity.class);
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
