package com.unimelb.breakout.fragment;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.gson.JsonSyntaxException;
import com.unimelb.breakout.R;
import com.unimelb.breakout.activity.MapSelectionActivity;
import com.unimelb.breakout.adapter.ScoreboardAdapter;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.MapMeta;
import com.unimelb.breakout.object.ScoreBoard;
import com.unimelb.breakout.object.UploadResponse;
import com.unimelb.breakout.preference.AccountPreference;
import com.unimelb.breakout.utils.AsyncUtils;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.view.BreakoutGame;
import com.unimelb.breakout.webservice.DataManager;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GlobalScoreBoardFragment extends Fragment{
	
	ListView listView;
	LinearLayout labels;
	TextView nodata ;
	
	
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GlobalScoreBoardFragment newInstance() {
    	GlobalScoreBoardFragment fragment = new GlobalScoreBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GlobalScoreBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_score_board, container, false);
        
        TextView playername = (TextView) view.findViewById(R.id.list_player_name);
        
        TextView label_one = (TextView) view.findViewById(R.id.list_label_one);
        TextView label_two = (TextView) view.findViewById(R.id.list_label_two);
        TextView label_three = (TextView) view.findViewById(R.id.list_label_three);
        
        label_one.setText("Name");
        label_two.setText("Score");
        label_three.setText("Rank");

        labels = (LinearLayout) view.findViewById(R.id.labels);
        nodata = (TextView) view.findViewById(R.id.label_nodata);
        
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
        
        listView = (ListView) view.findViewById(R.id.scorelist);  
        return view;
    }
    
    @Override
    public void onResume(){
    	super.onResume();
        downloadGlobalScoreboard();        
    }
    
    public void downloadGlobalScoreboard(){
		final Dialog loadingDialog = Utils.showLoadingDialog(getActivity(), "Querying high scores..");
		final ListenableFuture<ScoreBoard> scoreboard = DataManager.getScoreBoard();
		
		

		
		
        AsyncUtils.addCallback(scoreboard, new FutureCallback<ScoreBoard>() {
            @Override
            public void onSuccess(ScoreBoard sb) {
            	Log.d("MAPLISTFUTURE", "Query succeeds");
            	
                loadingDialog.dismiss();
                loadData(sb);
                if(sb.getScores().size() == 0){
                	labels.setVisibility(View.GONE);
             	   nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
            	loadingDialog.dismiss();
            	if(throwable instanceof SocketException){
            		Utils.showOkDialog(getActivity(), 
                			"Socket Exception", 
                			"Fail to build connection. Please try it late.");
            	}else if(throwable instanceof SocketTimeoutException){
            		Utils.showOkDialog(getActivity(), 
                			"Socket Timeout", 
                			"Connection is timeout. Please try it late.");
            	}else if(throwable instanceof JsonSyntaxException){
            		Utils.showOkDialog(getActivity(), 
                			"Query Failed", 
                			"Unexpected response from the server. Please try it later. ");
            	}else{
            		Utils.showOkDialog(getActivity(), 
                			"Query Failed", 
                			"Unknown error. Please try it later. ");
            	}
            	
            	labels.setVisibility(View.GONE);
         	    nodata.setVisibility(View.VISIBLE);
                
                Log.e("MAPLISTFUTURE", "Throwable during getscore:" + throwable);
            }
        });
		
	}
    
    public void loadData(ScoreBoard sb){
    	if(sb.getScores()!=null && !sb.getScores().isEmpty()){
    		ScoreboardAdapter adapter = new ScoreboardAdapter(getActivity(), R.layout.scorelist_item, sb.getScores());
            listView.setAdapter(adapter); 
            BreakoutGame.getInstance().setScoreboard(sb);
    	}   	 
    }
}
