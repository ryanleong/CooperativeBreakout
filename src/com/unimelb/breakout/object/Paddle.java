package com.unimelb.breakout.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.unimelb.breakout.R;
import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.view.WorldView;

public class Paddle extends Block{
	

	private volatile float dx;
	private float dy;
	
	private int screenWidth;
	private int screenHeight;
	
	private float MIN_X;
	private float MAX_X;
	
	
	public Paddle(WorldView worldView, Bitmap bitmap) {
		super(worldView, bitmap, worldView.initial_x, worldView.initial_y, worldView.paddle_w,  worldView.paddle_h, 0);
		screenWidth = worldView.width;
		screenHeight = worldView.height;
		MIN_X =  worldView.paddle_w/2;
		MAX_X = screenWidth - MIN_X;
		d = worldView.getResources().getDrawable(R.drawable.paddle_green);

	}

	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	private void updatePhysics() {
		// TODO Auto-generated method stub
		if(x<MIN_X){
			dx = 0;
			x = MIN_X;
		} else if(x>MAX_X){
			dx = 0;
			x = MAX_X;
		}				

	}
	
	@Override
	public void onDraw(Canvas canvas){
		
		updatePhysics();
		
		if(worldView.getOnScreen()){
			
			//updatePosition(x,y);
			//canvas.drawRect(x-width/2, y-height/2, x+width/2, y+height/2, getPaint(Color.GREEN));
			d.setBounds((int)(x-width/2), (int)(y-height/2), (int)(x+width/2), (int)(y+height/2));
			d.draw(canvas);
			
		}
	}
	
	private Paint getPaint(int color){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		return paint;
	}

	public boolean isXCovered(float x){
		if((x >= this.x - (width/2)) && (x <= (this.x + (width/2)))){
			return true;
		}
		return false;
	}
}
