package com.unimelb.breakout.activity;

import com.example.breakout.R;
import com.unimelb.breakout.view.WorldView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements WorldView.onBlockRemoveListener{
	
	private WorldView worldView;
	
	private TextView myScore;
	private int score = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		worldView = (WorldView) this.findViewById(R.id.main_worldView);

		myScore = (TextView) this.findViewById(R.id.main_text_myscore);

		//myScore.setText(score);
		
		worldView.setOnBlockRemoveListener(this);
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
	
}
