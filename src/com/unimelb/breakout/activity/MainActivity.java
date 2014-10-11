package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.object.MapMeta;
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
	
	private TextView myLives;
	private TextView myScore;
	private TextView myLevel;


	//private int lives = 3;
	
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


		}else{
			//challenge mode
			setContentView(R.layout.activity_main);
			
			worldView = (WorldView) this.findViewById(R.id.main_worldView);


		}
		
		
		worldView.setActivity(this);

		myScore = (TextView) this.findViewById(R.id.main_text_myscore);
		myLives = (TextView) this.findViewById(R.id.main_text_lives);
		myLevel = (TextView) this.findViewById(R.id.main_text_level);
		
		myLevel.setText(currentMap);
		//myScore.setText(score);
		
		worldView.setOnBlockRemoveListener(this);
		worldView.setOnLifeLostListener(this);
		worldView.setOnGameOverListener(this);
		worldView.setOnGameClearListener(this);
		
		dbHelper = new DBHelper(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		this.myScore.setText(Integer.toString(score));
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
		
		this.myLives.setText(Integer.toString(lives));
		
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
        dialog.setContentView(R.layout.dialog_gameover);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_gameover_title);
        TextView score = (TextView) dialog.findViewById(R.id.dialog_gameover_score);
        TextView next = (TextView) dialog.findViewById(R.id.dialog_gameover_next);

        
        Button home = (Button) dialog.findViewById(R.id.dialog_home_button);
        Button restart = (Button) dialog.findViewById(R.id.dialog_restart_button);

        title.setText("title");

        score.setText("You got " + this.score + " in level " + this.level +" game. Congratulations!..");
        next.setText("Your highest score is " + this.score);

        
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                recordScore();
                finish();
            }
        });

        restart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Log.i("MainActivity", "Game starts again");
            	dialog.dismiss();
            	recordScore();
            	
				worldView.restart();
            }
        });
        
        dialog.show();
	}
	
	
	public void recordScore(){
		
        dbHelper.insertScoreRecord(this.level, this.score);

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
                recordScore();
                finish();
            }
        });

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog.dismiss();
            	recordScore();
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
		        		myLevel.setText(currentMap);
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
