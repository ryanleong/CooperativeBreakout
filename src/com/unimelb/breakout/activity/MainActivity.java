package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.utils.DBHelper;
import com.unimelb.breakout.view.WorldView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
													  WorldView.onGameOverListener{
	private DBHelper dbHelper;
	private WorldView worldView;
	
	private TextView myScore;
	private int score = 0;
	private int level = 0;
	private int next = 2000;
	
	private TextView myLives;
	//private int lives = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		worldView = (WorldView) this.findViewById(R.id.main_worldView);

		myScore = (TextView) this.findViewById(R.id.main_text_myscore);
		myLives = (TextView) this.findViewById(R.id.main_text_lives);
		//myScore.setText(score);
		
		worldView.setOnBlockRemoveListener(this);
		worldView.setOnLifeLostListener(this);
		worldView.setOnGameOverListener(this);
		
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
	
	public void restart(){
		this.myScore.setText(Integer.toString(0));
		this.myLives.setText(Integer.toString(3));
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
}
