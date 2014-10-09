package com.unimelb.breakout.adapter;

import java.util.ArrayList;

import com.unimelb.breakout.R;
import com.unimelb.breakout.object.Rank;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScoreboardAdapter extends ArrayAdapter<Rank>{
	
	Context context; 
    int layoutResourceId; 
	ArrayList<Rank> data;
    /**
     * A custom ArrayAdapter to display scoreboard information
     * @param 
     * @param 
     * @param 
     */
    public ScoreboardAdapter(Context context, int viewGroupId, ArrayList<Rank> values) {
        super(context, R.layout.scorelist_item, values);
        this.layoutResourceId = viewGroupId;
        this.context = context;
        this.data = values;
    }
    
    @Override
    public int getCount() {
    	Log.d("tag", "Trying to get count on line 50 class Test");
        return data.size();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	View row = convertView;
        ViewHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new ViewHolder();
            holder.name = (TextView)row.findViewById(R.id.item_level);
            holder.score = (TextView)row.findViewById(R.id.item_score);
            holder.rank = (TextView)row.findViewById(R.id.item_time);
            
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        
        Rank rank = data.get(position);
        
        holder.name.setText(rank.getName());
        holder.score.setText(Integer.toString(rank.getScore()));
        holder.rank.setText(Integer.toString(rank.getRank()));
        
        return row;
//        LayoutInflater inflater = (LayoutInflater) getContext()
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        
//        View rowView = inflater.inflate(R.layout.scorelist_item, parent, false);
//        
//        TextView level = (TextView) rowView.findViewById(R.id.item_level);
//        TextView score = (TextView) rowView.findViewById(R.id.item_score);
//        TextView rank = (TextView) rowView.findViewById(R.id.item_time);
//
//        level.setText(getItem(position).getName());
//        score.setText(getItem(position).getScore());
//        rank.setText(getItem(position).getRank());
//
//        return rowView;
    }
    
    
    public static class ViewHolder{
    	TextView name;
    	TextView score;
    	TextView rank;
    }
    
}
