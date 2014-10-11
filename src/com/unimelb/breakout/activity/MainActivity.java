package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.object.MapMeta;
import com.unimelb.breakout.preference.AccountPreference;
import com.unimelb.breakout.utils.DBHelper;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.view.ArcadeWorldView;
import com.unimelb.breakout.view.WorldView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements WorldView.onBlockRemoveListener, 
													  WorldView.onLifeLostListener,
													  WorldView.onGameOverListener,
													  WorldView.onGameClearListener{
	private DBHelper dbHelper;
	private WorldView worldView;
	
	private String currentMap;
	
	private int score = 0;
	private int level = 0;
	private int next = 2000;
	
	private TextView mLives;
	private TextView mScore;
	private TextView mLevel;
	private TextView mPlayer;


	//private int lives = 3;
	
	private boolean isArcade = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = this.getIntent().getExtras();
		if(bundle != null){
			int screenOrientation = bundle.getInt("screenOrientation");
			
			switch(screenOrientation){
			
				case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
					setRequestedOrientation(screenOrientation);
					break;
				case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
					setRequestedOrientation(screenOrientation);
					break;
			}	
			
			currentMap = bundle.getString("map");
			

			
		}else{
			Utils.showOkDialog(this, "Error", "Unknown error occurs.");
		}
		if(currentMap.equals("1-0")){
			//arcade mode
			setContentView(R.layout.activity_main_arcade);
			worldView = (ArcadeWorldView) this.findViewById(R.id.main_worldView);
			isArcade = true;

		}else{
			//challenge mode
			setContentView(R.layout.activity_main);
			
			worldView = (WorldView) this.findViewById(R.id.main_worldView);
			isArcade = false;

		}
		
		
		worldView.setActivity(this);

		mScore = (TextView) this.findViewById(R.id.main_text_myscore);
		mLives = (TextView) this.findViewById(R.id.main_text_lives);
		mLevel = (TextView) this.findViewById(R.id.main_text_level);
		mPlayer = (TextView) this.findViewById(R.id.main_text_player);
		
		mLevel.setText(currentMap);
		//myScore.setText(score);
		
		if(AccountPreference.hasPlayerName()){
			String name = AccountPreference.getPlayerName();
			mPlayer.setText(name);
		}
		
		worldView.setOnBlockRemoveListener(this);
		worldView.setOnLifeLostListener(this);
		worldView.setOnGameOverListener(this);
		worldView.setOnGameClearListener(this);
		
		dbHelper = new DBHelper(this);
		
	}
	
	@Override
	public void onBackPressed () {
		super.onBackPressed();
		worldView.stopRunning();
	}

	@Override
	public void onBlockRemoved(final int s) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {

		 		addScore(s);

		    }
		});
	}
	
	public void addScore(int s){
		this.score+=s;
		this.mScore.setText(Integer.toString(score));
	}
	
	@Override
	public void onLifeLost(final int lives) {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 
		 		Log.i("MAIN ACTIVITY", "Lives: " + lives);

		 		lostLife(lives);

		    }
		});
	}
	
	public void lostLife(int lives){
		
		this.mLives.setText(Integer.toString(lives));
		
		Log.i("MAIN ACTIVITY 2", "Lives: " + lives);
		if(lives > 0){
			Toast toast = Toast.makeText(this.getApplicationContext(), lives + " lives remaining ...", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, -worldView.height/10);
			toast.show();
		}
	}

	@Override
	public void onGameOver() {
		// TODO Auto-generated method stub
		
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 
		 		Log.i("MAIN ACTIVITY", "Game Over");

		 		gameover();

		    }
		});
	}

	
	public void gameover(){

		final Dialog dialog = new Dialog(this, R.style.dialog_no_decoration);
		
		
		if(isArcade){
			//Arcade game is over
			dialog.setContentView(R.layout.dialog_gameover_arcade);

	        TextView title = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_title);
	        TextView player = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_player);
	        TextView score = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_score);
	        TextView rank = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_rank);
	        TextView detail = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_detail);
	        
	        Button upload = (Button) dialog.findViewById(R.id.dialog_upload_button);
	        Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel_button);

	        title.setText("GAME OVER");
	        player.setText(mPlayer.getText().toString());
	        score.setText(mScore.getText().toString());
	        rank.setText(mLevel.getText().toString());
	        detail.setText("You have a high score! Would you like to upload to the server to get ranked?");
	        
	        upload.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog.dismiss();
	                saveScoreInDB();
	                saveUploadedScoreinDB();
	                finish();
	            }
	        });

	        cancel.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	dialog.dismiss();
	            	saveScoreInDB();
	            	finish();
	            }
	        });
	        
	        
		}else{
			//Challenge game is over
	        dialog.setContentView(R.layout.dialog_gameover);
	
	        TextView title = (TextView) dialog.findViewById(R.id.dialog_gameover_title);
	        TextView player = (TextView) dialog.findViewById(R.id.dialog_gameover_player);
	        TextView score = (TextView) dialog.findViewById(R.id.dialog_gameover_score);
	        TextView level = (TextView) dialog.findViewById(R.id.dialog_gameover_level);
	
	        
	        Button home = (Button) dialog.findViewById(R.id.dialog_home_button);
	        Button restart = (Button) dialog.findViewById(R.id.dialog_restart_button);
	
	        title.setText("GAME OVER");
	        player.setText(mPlayer.getText().toString());
	        score.setText(mScore.getText().toString());
	        level.setText(mLevel.getText().toString());
	        
	        home.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                dialog.dismiss();
	                finish();
	            }
	        });
	
	        restart.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Log.i("MainActivity", "Game starts again");
	            	dialog.dismiss();
	            	
					worldView.restart();
	            }
	        });
		}
        dialog.show();
	}
	
	
	public void saveScoreInDB(){
		
        dbHelper.insertScoreRecord(this.level, this.score);
	}
	
	public void saveUploadedScoreinDB(){
        if(AccountPreference.hasScore()){
        	int s =  AccountPreference.getScore();
        	if(s < score){
        		 AccountPreference.rememberScore(this.score);
        	}
        }else{
            AccountPreference.rememberScore(this.score);
        }
        
	}
	
	
	
	public String getMap(){
		return currentMap;
	}

	@Override
	public void onGameClear() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 
		 		Log.i("MAIN ACTIVITY", "Game Clear");

		 		gameclear();

		    }
		});
	}
	
	public void gameclear(){
		
		final Dialog dialog = new Dialog(this, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_gameclear);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_gameclear_title);
        TextView score = (TextView) dialog.findViewById(R.id.dialog_gameclear_score);
        TextView nex = (TextView) dialog.findViewById(R.id.dialog_gameclear_next);

        
        Button home = (Button) dialog.findViewById(R.id.dialog_home_button);
        Button next = (Button) dialog.findViewById(R.id.dialog_next_button);

        title.setText("title");

        score.setText("You got " + this.score + " in level " + this.level +" game. Congratulations!..");
        nex.setText("Your highest score is " + this.score);

        
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog.dismiss();
            	MapMeta nextLevel = LocalMapUtils.getNextLevel(currentMap, MainActivity.this);
            	
            	if(nextLevel != null){
            		Log.d("MAINACTIBITY", nextLevel.getType().equals("remote")+"");
            		Log.d("MAINACTIBITY", LocalMapUtils.hasDownloaded(nextLevel.getName(), MainActivity.this)+"");

            		if(nextLevel.getType().equals("remote") && !LocalMapUtils.hasDownloaded(nextLevel.getName(), MainActivity.this)){
            				Dialog ok = Utils.showOkDialog(MainActivity.this, "New Map", "You need to download the next level from the server.");
            				ok.setOnDismissListener(new OnDismissListener(){

								@Override
								public void onDismiss(DialogInterface dialog) {
									// TODO Auto-generated method stub
									finish();
								}
            					
            				});
            				//finish();
            		}else{
            		
		            	Log.d("MAINACTIVITY", "Next Level is " + nextLevel);
		            	currentMap = nextLevel.getName();
		        		mLevel.setText(currentMap);
						worldView.restart();
						Log.d("MAINACTIVITY", "Next Level game starts");
					}
				}else{
					Utils.showOkDialog(MainActivity.this, "Congratulations!", "You have clear all stages!");
				}         	
            }
        });
        
        dialog.show();
	}
}
