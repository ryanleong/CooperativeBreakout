package com.unimelb.breakout.activity;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonSyntaxException;
import com.unimelb.breakout.R;
import com.unimelb.breakout.object.MapMeta;
import com.unimelb.breakout.object.Rank;
import com.unimelb.breakout.object.ScoreBoard;
import com.unimelb.breakout.object.UploadResponse;
import com.unimelb.breakout.preference.AccountPreference;
import com.unimelb.breakout.utils.AsyncUtils;
import com.unimelb.breakout.utils.DBHelper;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.view.ArcadeWorldView;
import com.unimelb.breakout.view.BreakoutGame;
import com.unimelb.breakout.view.WorldView;
import com.unimelb.breakout.webservice.DataManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
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
													  WorldView.onGameClearListener,
													  ArcadeWorldView.onDifficultyIncreaseListener{
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
	private TextView mRank;
	private TextView mNext;


	//private int lives = 3;
	
	private boolean isArcade = false;
	private ScoreBoard  scoreboard = null;
	private int rank = 10;
	private String name;
	
	
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
		
		mScore = (TextView) this.findViewById(R.id.main_text_myscore);
		mLives = (TextView) this.findViewById(R.id.main_text_lives);
		mLevel = (TextView) this.findViewById(R.id.main_text_level);
		mPlayer = (TextView) this.findViewById(R.id.main_text_player);
		mRank = (TextView) this.findViewById(R.id.main_text_rank);
		mNext = (TextView) this.findViewById(R.id.main_text_nextscore);
		
		mLevel.setText(currentMap);
		
		if(isArcade){
			getScoreboard();
			displayRank();
			((ArcadeWorldView) worldView).setOnDifficultyIncreaseListener(this);
 		}
		
		worldView.setActivity(this);

		//myScore.setText(score);
		
		if(AccountPreference.hasPlayerName()){
			String name = AccountPreference.getPlayerName();
			this.name = name;
			mPlayer.setText(name);
		}else{
			Utils.showPlayerName(this, "Please have a name before you start.");
			String name = AccountPreference.getPlayerName();
			this.name = name;
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
		if(isArcade){
			displayRank();
		}
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

	        TextView titleView = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_title);
	        TextView playerView = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_player);
	        TextView scoreView = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_score);
	        TextView rankView = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_rank);
	        TextView detailView = (TextView) dialog.findViewById(R.id.dialog_gameover_arcade_detail);
	        
	        Button upload = (Button) dialog.findViewById(R.id.dialog_upload_button);
	        Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel_button);

	        titleView.setText("GAME OVER");
	        playerView.setText(mPlayer.getText().toString());
	        scoreView.setText(mScore.getText().toString());
	        rankView.setText(mLevel.getText().toString());
	        
	        if(!mRank.getText().equals("-")){
		        detailView.setText("You have a high score! Would you like to upload to the server to get ranked?");
		        
		        upload.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		                dialog.dismiss();
		                saveScoreInDB();
		                finish();
		                uploadScore(name, score);
		            }
		        });
	        }else{
	        	detailView.setVisibility(View.GONE);
	        	detailView.setClickable(false);
	        	
	        	upload.setVisibility(View.GONE);
	        	upload.setClickable(false);	        	
	        }


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
	
	        TextView titleView = (TextView) dialog.findViewById(R.id.dialog_gameover_title);
	        TextView playerView = (TextView) dialog.findViewById(R.id.dialog_gameover_player);
	        TextView scoreView = (TextView) dialog.findViewById(R.id.dialog_gameover_score);
	        TextView levelView = (TextView) dialog.findViewById(R.id.dialog_gameover_level);
	
	        
	        Button home = (Button) dialog.findViewById(R.id.dialog_home_button);
	        Button restart = (Button) dialog.findViewById(R.id.dialog_restart_button);
	
	        titleView.setText("GAME OVER");
	        playerView.setText(mPlayer.getText().toString());
	        scoreView.setText(mScore.getText().toString());
	        levelView.setText(mLevel.getText().toString());
	        
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
		dialog.setCancelable(false);
		
        dialog.show();
	}
	
	public void uploadScore(String name, int score){
		final Dialog loadingDialog = Utils.showLoadingDialog(this, "Uploading Score..");

		final ListenableFuture<UploadResponse> response = DataManager.uploadNewScore(name, score);
		AsyncUtils.addCallback(response, new FutureCallback<UploadResponse>() {
            @Override
            public void onSuccess(UploadResponse sb) {
            	Log.d("MAPLISTFUTURE", "Download succeeds");
            	
                loadingDialog.dismiss();

            	if(sb.isSuccess()){
                    saveUploadedScoreinDB();
            	}else{
            		Utils.showOkDialog(MainActivity.this, 
                			"Upload Failed", 
                			"Server error. Please try it late.");
            	}

            }

            @Override
            public void onFailure(Throwable throwable) {
            	loadingDialog.dismiss();
            	if(throwable instanceof SocketException){
            		Utils.showOkDialog(MainActivity.this, 
                			"Socket Exception", 
                			"Cannot connect to the server. Upload failed.");
            	}else if(throwable instanceof SocketTimeoutException){
            		Utils.showOkDialog(MainActivity.this, 
                			"Socket Timeout", 
                			"Cannot connect to the server. Upload failed.");
            	}else if(throwable instanceof JsonSyntaxException){
            		Utils.showOkDialog(MainActivity.this, 
                			"JSON Parse Exception", 
                			"Unexpected response from the server. Upload failed.");
            	}else{
            		Utils.showOkDialog(MainActivity.this, 
                			"Unknown error.", 
                			"Upload failed due to unknown error.");
            	}
            	
                Log.e("MAPLISTFUTURE", "Throwable during write score:" + throwable);
            }
        });
	}
	
	public void getScoreboard(){
		ScoreBoard sb = BreakoutGame.getInstance().getScoreboard();
		
		if(sb == null){
			//final Dialog loadingDialog = Utils.showLoadingDialog(this, "Querying high scores..");
			final ListenableFuture<ScoreBoard> listenablescoreboard = DataManager.getScoreBoard();
			
	        AsyncUtils.addCallback(listenablescoreboard, new FutureCallback<ScoreBoard>() {
	            @Override
	            public void onSuccess(ScoreBoard sboard) {
	            	Log.d("MAPLISTFUTURE", "Query succeeds");
	            	scoreboard = sboard;
	            	rank = scoreboard.getScores().size();
	                //loadingDialog.dismiss();
	                BreakoutGame.getInstance().setScoreboard(sboard);
	                displayRank();
	            }

	            @Override
	            public void onFailure(Throwable throwable) {
//	            	if(loadingDialog != null)
//	            		loadingDialog.dismiss();
	            	if(throwable instanceof SocketException){
	            		Utils.showOkDialog(MainActivity.this, 
	                			"Socket Exception", 
	                			"Fail to connect the server. Your score will not be uploaded to the server.");
	            	}else if(throwable instanceof SocketTimeoutException){
	            		Utils.showOkDialog(MainActivity.this, 
	                			"Socket Timeout", 
	                			"Connection is timeout. Your score will not be uploaded to the server.");
	            	}else if(throwable instanceof JsonSyntaxException){
	            		Utils.showOkDialog(MainActivity.this, 
	                			"Query Failed", 
	                			"Unexpected response from the server. Your score will not be uploaded to the server. ");
	            	}else{
	            		Utils.showOkDialog(MainActivity.this, 
	                			"Query Failed", 
	                			"Due to unknown error, your score will not be uploaded to the server.");
	            	}
	                Log.e("MAPLISTFUTURE", "Throwable during getscore:" + throwable);
	            }
	        });
		}else{
			scoreboard = sb;
        	rank = scoreboard.getScores().size();
		}
		
	}
	
	public void displayRank(){
		if(scoreboard != null){
			ArrayList<Rank> ranks = scoreboard.getScores();
			Rank r = ranks.get(ranks.size()-1);
			int rank = Integer.MAX_VALUE;
			Log.d("RANK", ""+this.rank);
			while((r = ranks.get(this.rank - 1)).getScore() < this.score){
				rank = r.getRank();
				this.rank--;				
			}
			
			if(rank <= 10){
				mRank.setText(Integer.toString(rank));
			}
			
			mNext.setText(Integer.toString(r.getScore() - this.score));		
		}
	}
	
	public void setScoreBoard(ScoreBoard sb){
		
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
	

	@Override
	public void onDifficultyIncrease() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
		     @Override
		     public void run() {
		    	 
		 		Log.i("MAIN ACTIVITY", "Arcade Game Clear");

				Dialog dialog = Utils.showOkDialog(MainActivity.this, "Congratulation", "You have cleared all blocks. Now, the difficulty has increased.");
				dialog.setOnDismissListener(new OnDismissListener(){

					@Override
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						((ArcadeWorldView) worldView).increaseDiff();
					}
					
				});
		    }
		});
	}
	
	public void gameclear(){
		
		final Dialog dialog = new Dialog(this, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_gameclear);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_gameclear_title);
        TextView name = (TextView) dialog.findViewById(R.id.dialog_gameclear_player);
        TextView score = (TextView) dialog.findViewById(R.id.dialog_gameclear_score);
        TextView level = (TextView) dialog.findViewById(R.id.dialog_gameclear_level);

        
        Button home = (Button) dialog.findViewById(R.id.dialog_home_button);
        Button next = (Button) dialog.findViewById(R.id.dialog_next_button);

        title.setText("Level Clear");

        name.setText(this.name);
        score.setText(Integer.toString(this.score));
        level.setText(this.currentMap);

        
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
