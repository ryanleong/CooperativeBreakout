package com.unimelb.breakout.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.view.WorldView;

public class Block {
	public float width;
	public float height;
	
	public WorldView worldView;
	public Bitmap bitmap;
	
	public float x;
	public float y;
	
	public Block(WorldView worldView, Bitmap bitmap, float x, float y, float width, float height) {
		super();
		this.worldView = worldView;
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void onDraw(Canvas canvas){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
				
		if(worldView.getOnScreen()){
			
			canvas.drawRect(x-width/2, y-height/2, x+width/2, y+height/2, paint);
		}
	}
	
	public boolean isCovered(float x, float y){
		if(x >= this.x && x <= (this.x + this.width) && y >= this.y && y <= (this.y + this.height)){
			return true;
		}
		return false;
	}
	
	public void bounce(Ball ball){
	float WIDTH = 2*ball.getRadius() + this.width;
	float HEIGHT = 2*ball.getRadius() + this.height;
	
	float angle = HEIGHT/WIDTH;
	
	float x = ball.getX() - (this.getX() + this.getWidth()/2);
	float y = ball.getY() - (this.getY() + this.getHeight()/2);

	if(x == 0){
		ball.yBounce();
	}else if(angle == Math.abs(y/x)){
		ball.xBounce(0);
		ball.yBounce();
	}else if(angle > Math.abs(y/x)){
		ball.xBounce(0);
	}else{
		ball.yBounce();
	}
	
		
}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public Point[] getBoxPoints(){
		return new Point[]
				{
				 new Point(x+width/2,y-height/2), //right-top corner point
				 new Point(x+width/2,y+height/2), //right-bottom corner point
				 new Point(x-width/2,y+height/2), //left-bottom corner point
				 new Point(x-width/2,y-height/2)};//left-top corner point
	}
	
	public Point getNearestPoint(float x, float y){

		Point points[] = getBoxPoints();
		Point out = null;
		double distance = Float.POSITIVE_INFINITY;
		for(Point point: points){
			double d = Math.sqrt(Math.pow(x-point.x, 2) + Math.pow(y-point.y, 2));
			if(distance > d){
				out = point;
				distance = d;
			}
		}
		
		return out;
	}
}