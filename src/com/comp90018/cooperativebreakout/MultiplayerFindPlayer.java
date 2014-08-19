package com.comp90018.cooperativebreakout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MultiplayerFindPlayer extends Activity implements OnClickListener{
	
	private Button btnLevelSelect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		                                WindowManager.LayoutParams.FLAG_FULLSCREEN);     
		
		setContentView(R.layout.activity_multiplayer_find_player);
		
		btnLevelSelect = (Button) findViewById(R.id.btnMultiplayerLevelSelect);
		btnLevelSelect.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multiplayer_find_player, menu);
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
	public void onClick(View v) {
		// getId() returns this view's identifier.
		if (v.getId() == R.id.btnMultiplayerLevelSelect) {

			Intent multiplayerLevelSelect = new Intent(v.getContext(), MultiplayerLevelSelect.class);
			startActivity(multiplayerLevelSelect);
			
		}
	}
}
