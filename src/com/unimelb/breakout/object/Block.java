package com.unimelb.breakout.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.unimelb.breakout.R;
import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.view.WorldView;

public class Block {
	public float width;
	public float height;
	
	public WorldView worldView;
	public Bitmap bitmap;
	
	public float x;
	public float y;
	
	public float left_edge;
	public float right_edge;
	public float top_edge;
	public float bottom_edge;
	
	Paint paint;
	Drawable d;
	int i;
	
	public Block(WorldView worldView, Bitmap bitmap, float x, float y, float width, float height, int i) {
		super();
		this.worldView = worldView;
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.i = i;
		
		this.left_edge = x - width/2;
		this.right_edge = x + width/2;
		this.top_edge = y - height/2;
		this.bottom_edge = y + height/2;
		
		paint = new Paint();
		paint.setAntiAlias(true);
		
		int colors[] = {Color.RED, Color.MAGENTA, Color.YELLOW, Color.DKGRAY, Color.CYAN};
		
		d = worldView.getResources().getDrawable(R.drawable.brick);
		paint.setColor(colors[i%5]);
	}

//	public float getX() {
//		return x;
//	}
//
//	public void setX(float x) {
//		this.x = x;
//	}
//
//	public float getY() {
//		return y;
//	}
//
//	public void setY(float y) {
//		this.y = y;
//	}
	
	public void onDraw(Canvas canvas){
				
		if(worldView.getOnScreen()){
			
			//canvas.drawRect(this.left_edge, this.top_edge, this.right_edge, this.bottom_edge, paint);
			d.setBounds((int)(this.left_edge), (int)(this.top_edge), (int)(this.right_edge), (int)(this.bottom_edge));
			d.draw(canvas);
		}
	}
	
	
	public boolean isCovered(float x, float y){
		if(x >= this.x && x <= (this.x + this.width) && y >= this.y && y <= (this.y + this.height)){
			return true;
		}
		return false;
	}
	
//	public void bounce(Ball ball){
//	float WIDTH = 2*ball.r + this.width;
//	float HEIGHT = 2*ball.r + this.height;
//	
//	float angle = HEIGHT/WIDTH;
//	
//	float x = ball.x - (this.getX() + this.width/2);
//	float y = ball.y - (this.getY() + this.height/2);
//
//	if(x == 0){
//		ball.yBounce();
//	}else if(angle == Math.abs(y/x)){
//		ball.xBounce(0);
//		ball.yBounce();
//	}else if(angle > Math.abs(y/x)){
//		ball.xBounce(0);
//	}else{
//		ball.yBounce();
//	}
//	
//		
//}
//	
	
	public void moveDown(){
		this.y += this.height;
		this.top_edge = y - height/2;
		this.bottom_edge = y + height/2;
	}
	public Point[] getBoxPoints(){
		return new Point[]
				{
				 new Point(right_edge,top_edge), //right-top corner point
				 new Point(right_edge,bottom_edge), //right-bottom corner point
				 new Point(left_edge,bottom_edge), //left-bottom corner point
				 new Point(left_edge/2,top_edge)};//left-top corner point
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
