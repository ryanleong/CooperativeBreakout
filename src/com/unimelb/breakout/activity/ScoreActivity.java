package com.unimelb.breakout.activity;

import com.unimelb.breakout.R;
import com.unimelb.breakout.utils.DBHelper;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class ScoreActivity extends Activity{

	DBHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scorelist);
		

	       	
	       	
	    	//this.deleteDatabase(DBHelper.dbName);
	    	dbHelper = new DBHelper(this);  

	        ListView listView = (ListView) this.findViewById(R.id.scorelist);  
	          
	        Cursor cursor = dbHelper.fetchBestTenRecords();
	        
	        String[] columns = {DBHelper.LEVEL, DBHelper.SCORE, DBHelper.TIME};
	        
	        int[] fields = {R.id.item_level, R.id.item_score, R.id.item_time};
	        
	        
	       SimpleCursorAdapter adapter = new SimpleCursorAdapter(
	    		    this, 
	    		    R.layout.scorelist_item, 
	    		    cursor, 
	    		    columns, 
	    		    fields,
	    		    0);
	       
	       listView.setAdapter(adapter);
	       
	}
}
