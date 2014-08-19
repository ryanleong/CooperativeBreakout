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
import android.widget.Toast;

public class MainMenu extends Activity implements OnClickListener {

	private Button btnSinglePlayer, btnMultiplayer, btnDownloadMaps, btnHighscores;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set Fullscreen
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		                                WindowManager.LayoutParams.FLAG_FULLSCREEN);     
		
		setContentView(R.layout.activity_main_menu);

		// Initialize Buttons
		btnSinglePlayer = (Button) findViewById(R.id.btnSinglePlayer);
		btnMultiplayer = (Button) findViewById(R.id.btnMultiplayer);
		btnDownloadMaps = (Button) findViewById(R.id.btnDownloadMaps);
		btnHighscores = (Button) findViewById(R.id.btnHighscores);

		btnSinglePlayer.setOnClickListener(this);
		btnMultiplayer.setOnClickListener(this);
		btnDownloadMaps.setOnClickListener(this);
		btnHighscores.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
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
		if (v.getId() == R.id.btnSinglePlayer) {

			startActivity(new Intent(v.getContext(), SinglePlayerLevelSelect.class));
			
		} else if (v.getId() == R.id.btnMultiplayer) {
			
			startActivity(new Intent(v.getContext(), MultiplayerFindPlayer.class));
			
		} else if (v.getId() == R.id.btnDownloadMaps) {
			
			Toast.makeText(getApplicationContext(), "Download Maps Menu",
					   Toast.LENGTH_LONG).show();
			
			startActivity(new Intent(v.getContext(), DownloadMapsMenu.class));

		} else if (v.getId() == R.id.btnHighscores) {
			
			Toast.makeText(getApplicationContext(), "This has not been implemented yet.",
					   Toast.LENGTH_LONG).show();
		}
	}
}
