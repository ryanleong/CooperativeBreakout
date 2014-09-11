package com.unimelb.breakout.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.view.WorldView;

public class Paddle extends Block{
	
	private volatile float xspeed;
	private float yspeed;
	
	private int screenWidth;
	private int screenHeight;
	private Paint paddlePaint;
	
	private float MIN_X;
	private float MAX_X;
	
	public Paddle(WorldView worldView, Bitmap bitmap) {
		super(worldView, bitmap, worldView.initial_x, worldView.initial_y, worldView.paddle_w,  worldView.paddle_h);
		screenWidth = worldView.width;
		screenHeight = worldView.height;
		MIN_X =  worldView.paddle_w/2;
		MAX_X = screenWidth - MIN_X;
;
	}

	public float getXspeed() {
		return xspeed;
	}

	public void setXspeed(float xspeed) {
		this.xspeed = xspeed;
	}

	public float getYspeed() {
		return yspeed;
	}

	public void setYspeed(float yspeed) {
		this.yspeed = yspeed;
	}
	
//	private void updatePosition(float x2, float y2) {
//		// TODO Auto-generated method stub
//		this.setX(x2 + this.xspeed);
//		//this.setY(y2 + this.yspeed); 
//	}
//	
	private void updatePhysics() {
		// TODO Auto-generated method stub
		if(x<MIN_X){
			this.setXspeed(0);
			x = MIN_X;
		} else if(x>MAX_X){
			this.setXspeed(0);
			x = MAX_X;
		}				

	}
	
	@Override
	public void onDraw(Canvas canvas){
		
		updatePhysics();
		
		if(worldView.getOnScreen()){
			
			//updatePosition(x,y);
			canvas.drawRect(x-width/2, y-height/2, x+width/2, y+height/2, getPaint(Color.GREEN));
		}
	}
	
	private Paint getPaint(int color){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		return paint;
	}
	
	public boolean isCovered(float x, float y){
		if((x >= this.x - (width/2)) && (x <= (this.x + (width/2))) && (y >= this.y - (height/2)) && (y <= (this.y + height/2))){
			return true;
		}
		return false;
	}
	
	public boolean isXCovered(float x){
		if((x >= this.x - (width/2)) && (x <= (this.x + (width/2)))){
			return true;
		}
		return false;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setLength(float length) {
		this.width = length;
	}
	
	public void movePaddle(float x, float y){
		this.setX(x);
		this.setY(y);
	}
	
	public boolean collisionOnTop(Ball ball){
		if(ball.getY() >= this.getY() - this.getHeight()/2 - ball.getRadius() && ball.getY() <= this.getY()){
			return true;
		}
		return false;
	}
	
	public boolean collisionOnBot(Ball ball){
		if(ball.getY() >= this.getY() && ball.getY() <= this.getY() + this.getHeight()/2 + ball.getRadius()){
			return true;
		}
		return false;
	}
	
	public boolean collisionOnLeft(Ball ball){
		if(ball.getX() >= this.getX() - this.getWidth()/2 - ball.getRadius()&& ball.getX() <= this.getX()){
			return true;
		}
		return false;
	}
	
	public boolean collisionOnRight(Ball ball){
		if(ball.getX() >= this.getX() && ball.getX() <= this.getX() + this.getWidth()/2 + ball.getRadius()){
			return true;
		}
		return false;
	}

//	public boolean collide(Ball ball) {
//		// TODO Auto-generated method stub
//		Point b_a = ball.getLastPosition();
//		Point b_b = ball.getPosition();
//		Point p_a = new Point(x+width/2, y-height/2);
//		Point p_b = new Point(x-width/2, y-height/2);
//		
//		if(Utils.intersect(b_a, b_b, p_a, p_b)==null){
//			return false;
//		}else{
//			return true;
//		}
//		
//	}

}
