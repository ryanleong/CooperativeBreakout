package com.unimelb.breakout.fragment;

import com.unimelb.breakout.R;
import com.unimelb.breakout.preference.AccountPreference;
import com.unimelb.breakout.utils.DBHelper;
import com.unimelb.breakout.utils.Utils;

import android.app.Fragment;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LocalScoreBoardFragment extends Fragment {

	//the database helper that can be used to make database query.
	DBHelper dbHelper;

	
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalScoreBoardFragment newInstance() {
    	LocalScoreBoardFragment fragment = new LocalScoreBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LocalScoreBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_score_board, container, false);
    	
        TextView playername = (TextView) view.findViewById(R.id.list_player_name);
       
        if(AccountPreference.hasPlayerName()){
        	String name = AccountPreference.getPlayerName();
        	playername.setText(name);
        }else{
        	playername.setText("Touch to set a player name.");
        }
        
        playername.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            Utils.showPlayerName(getActivity(), "What's your name?");
			}
    		
    	});
        
    	//this.deleteDatabase(DBHelper.dbName);
    	dbHelper = new DBHelper(getActivity());  

        ListView listView = (ListView) view.findViewById(R.id.scorelist);  
        LinearLayout labels = (LinearLayout) view.findViewById(R.id.labels);
        TextView nodata = (TextView) view.findViewById(R.id.label_nodata);
          
        Cursor cursor = dbHelper.fetchBestTenRecords();
        
        String[] columns = {DBHelper.LEVEL, DBHelper.SCORE, DBHelper.TIME};
        
        int[] fields = {R.id.item_level, R.id.item_score, R.id.item_time};
        
        
       SimpleCursorAdapter adapter = new SimpleCursorAdapter(
    		    getActivity(), 
    		    R.layout.scorelist_item, 
    		    cursor, 
    		    columns, 
    		    fields,
    		    0);
       
       listView.setAdapter(adapter);
       
       if(cursor.getCount() == 0){
    	   labels.setVisibility(View.GONE);
    	   nodata.setVisibility(View.VISIBLE);
       }
       
       return view;
               
    }
    
    
}
