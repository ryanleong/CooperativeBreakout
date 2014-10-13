package com.unimelb.breakout.utils;

import com.unimelb.breakout.R;
import com.unimelb.breakout.activity.MainActivity;
import com.unimelb.breakout.activity.MapSelectionActivity;
import com.unimelb.breakout.preference.AccountPreference;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Utils {

	/*
	 *Calculate the intersect point of two lines. 
	 *One starts from a1 to a2 and another starts from b1 to b2.
	 */
	public static Point intersect(float x1, float y1,
								float x2, float y2,
								float x3, float y3,
								float x4, float y4){
//	public static Point intersect(Point a1, Point a2, Point b1, Point b2){
//		
//		float x1 = a1.x;
//		float x2 = a2.x;
//		float x3 = b1.x;
//		float x4 = b2.x;
//		float y1 = a1.y;
//		float y2 = a2.y;
//		float y3 = b1.y;
//		float y4 = b2.y;
		
		float denom  = ((y4-y3) * (x2-x1)) - ((x4-x3) * (y2-y1));
		  if (denom != 0) {
		    float ua = (((x4-x3) * (y1-y3)) - ((y4-y3) * (x1-x3))) / denom;
		    if ((ua >= 0) && (ua <= 1)) {
		      float ub = (((x2-x1) * (y1-y3)) - ((y2-y1) * (x1-x3))) / denom;
		      if ((ub >= 0) && (ub <= 1)) {
		        float x = x1 + (ua * (x2-x1));
		        float y = y1 + (ua * (y2-y1));
		        return new Point(x,y);
		      }
		    }
		  }
		
		return null;
	}
	
	/**
     * Creates a loading dialog. When loading is complete, dismiss() should be called on the returned dialog.
     * @param context
     * @param text
     * @return the loading dialog.
     */
    public static Dialog showLoadingDialog(Context context, String text) {
        final Dialog dialog = new Dialog(context, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_loading);

        // TODO: uncomment when loading dialog functionality is fully implemented
        //dialog.setCancelable(false);
        //dialog.setCanceledOnTouchOutside(false);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_loading_title);
        title.setText(text);

        dialog.show();
        return dialog;
    }
    
    /**
     * Shows a simple okay dialog in the centre of the screen.
     *
     * @param context Context to display on.
     * @param titleText Title of the dialog.
     * @param detailText Detail text of the dialog.
     * @return the ok dialog.
     */
    public static Dialog showOkDialog(Context context, String titleText, String detailText) {
        final Dialog dialog = new Dialog(context, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_ok);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_ok_title);
        TextView detail = (TextView) dialog.findViewById(R.id.dialog_ok_detail);
        Button ok = (Button) dialog.findViewById(R.id.dialog_ok_button);

        title.setText(titleText);
        detail.setText(detailText);

        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        return dialog;
    }
    
    public static Dialog showPlayerName(Context context, String titleText){
    	final Dialog dialog = new Dialog(context, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_playername);

        final TextView title = (TextView) dialog.findViewById(R.id.dialog_playername_title);
        final EditText playername = (EditText) dialog.findViewById(R.id.dialog_playername_edit);
        
        Button ok = (Button) dialog.findViewById(R.id.dialog_ok_button);
        
        title.setText(titleText);

        
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = playername.getText().toString();
                if(name == null || name.isEmpty()){
                	playername.setError("The name cannot be empty.");
                }else{
                	AccountPreference.rememberPlayerName(name);
                    dialog.dismiss();
                }             
            }
        });
        
        dialog.show();
        
        return dialog;
    }
    
	public static Dialog chooseScreenOrientation(final Context context, final String mapName){
    	
		final Dialog dialog = new Dialog(context, R.style.dialog_no_decoration);
        dialog.setContentView(R.layout.dialog_screenorientation);

        TextView title = (TextView) dialog.findViewById(R.id.dialog_screenorientation_title);
        TextView detail = (TextView) dialog.findViewById(R.id.dialog_screenorientation_detail);
        
        Button portrait = (Button) dialog.findViewById(R.id.dialog_portrait_button);
        Button landscape = (Button) dialog.findViewById(R.id.dialog_landscape_button);

        title.setText("Choose the screen mode");

        detail.setText("Please choose the screen mode you would like to play with.");

		final Intent intent = new Intent(context, MainActivity.class);
		
        portrait.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                intent.putExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                intent.putExtra("map", mapName);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);        

            }
        });

        landscape.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog.dismiss();
                intent.putExtra("screenOrientation", ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                intent.putExtra("map", mapName);
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);        

            }
        });
        
        dialog.show();
        
        return dialog;
    }
}
