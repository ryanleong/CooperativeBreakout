package com.unimelb.breakout.fragment;

import com.unimelb.breakout.R;
import com.unimelb.breakout.utils.DBHelper;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
    	
    	//this.deleteDatabase(DBHelper.dbName);
    	dbHelper = new DBHelper(getActivity());  

        ListView listView = (ListView) view.findViewById(R.id.scorelist);  
          
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
       
       return view;
               
    }
    
    
}
