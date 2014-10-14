package com.unimelb.breakout.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.unimelb.breakout.R;
import com.unimelb.breakout.enums.GameState;
import com.unimelb.breakout.view.WorldView;

/**
 * The moving ball in the game
 * @author Siyuan Zhang
 *
 */
public class Ball {
	
	private WorldView worldView;
	
	//x coordinate
	public volatile float x;
	//y coordinate
	public volatile float y;
	//ball radius
	public float r;
	
	//x speed
	public volatile float dx;
	//y speed
	public volatile float dy;
	//acceleration
	public volatile float da = 0;
		
	//private boolean isActive;
	GameState state;
	
	private float INITIAL_SPEED_X = 10;
	private float INITIAL_SPEED_Y = 2;
	private float MAX_SPEED_X = 50;
	private float MAX_SPEED_Y = 50;
	private float MIN_X;
	private float MAX_X;
	private float MIN_Y;
	private float MAX_Y;
	
	private int screenWidth;
	private int screenHeight;
	Drawable d;
	
	public Ball(WorldView worldView, Bitmap bitmap){
		
		this.worldView = worldView;
		this.screenWidth = worldView.width;
		this.screenHeight = worldView.height;
		this.r = worldView.ball_r;
		this.MIN_X = worldView.wallWidth+r;
		this.MAX_X = screenWidth - MIN_X;
		this.MIN_Y = worldView.wallWidth+r;
		this.MAX_Y = screenHeight - MIN_Y;
		
		d = worldView.getResources().getDrawable(R.drawable.ball_red);

		
		this.dx = 0;
		this.dy = 0;
		this.da = 0;
		//setXspeed(INITIAL_SPEED_X);
		//setYspeed(INITIAL_SPEED_Y);
		
		this.state = GameState.READY;
		
	}
	
	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}

	public void activate(){
		this.state = GameState.ACTIVE;
		
	}
	
	public void ready(){
		this.state = GameState.READY;
	}
	
	public void end(){
		Log.i("BALL", "Lost One Life");
		this.state = GameState.DEAD;
	}
	
	public boolean isActive(){
		if(this.state == GameState.ACTIVE){
			return true;
		}
		return false;
	}
	
	public boolean isEnd(){
		if(this.state == GameState.DEAD){
			return true;
		}
		return false;
	}

	public void setXspeed(float xspeed) {
		if(Math.abs(xspeed) >= MAX_SPEED_X){
			if(xspeed > 0){
				this.dx = MAX_SPEED_X;
			}else{
				this.dx = -MAX_SPEED_X;
			}
		}else{
			this.dx = xspeed;
		}
	}
	
	//update the postion of the ball
	public void update(float dt){
		if (y >= screenHeight){
			end();
		}
		
		if (isActive()) {
			
	        updatePosition(dt);
	        handleWallCollision();
	        
		}
	}
	
	//handle collision with the wall
	public void handleWallCollision(){
	      
		if(x > MAX_X && dx > 0){
			x = MAX_X;
			this.xBounce(0);
			
		}else if(x < MIN_X && dx < 0){
			x = MIN_X;
			this.xBounce(0);
			
		}else if(y < MIN_Y && dy < 0){
			y = MIN_Y;
			this.yBounce();
		}
	}

	private void updatePosition(float dt) {
		// TODO Auto-generated method stub
		
		this.x = x + (dt * dx) + (da * dt * dt * 0.5f);
		this.y  = y + (dt * dy) + (da * dt * dt * 0.5f);
		this.dx = dx + (da * dt) * (dx > 0 ? 1 : -1);
		this.dy = dy + (da * dt) * (dy > 0 ? 1 : -1);
	}
	
	public void yBounce(){
		dy = -dy;
	}

	/**
	 * Slow down the ball if it moves too fast
	 * @param acceleration
	 */
	public void xBounce(float acceleration){
		if(Math.abs(dx) > INITIAL_SPEED_X){
			if(dx >= 0){
				dx -= 2;
			}else{
				dx += 2;
			}
		}
		dx = -dx + acceleration;
	}
	
	public void onDraw(Canvas canvas){
		
		update(1);
			
		if(worldView.getOnScreen()){
			
			//canvas.drawCircle(x, y, r, this.ballPaint);
			d.setBounds((int)(x-r), (int)(y-r), (int)(x+r), (int)(y+r));
			d.draw(canvas);
		}
	}

	public void bounceBlock(Block block){
		Log.d("BALL", "BOUNCE CALLED");
		
		if(this.dx == 0){
			this.yBounce();
		}else if(this.dy == 0){
			this.xBounce(0);
		}else{
			float WIDTH = 2*r + block.width;
			float HEIGHT = 2*this.r + block.height;
			
			double angle = HEIGHT/WIDTH;
			
			double x = Math.abs(this.x - block.x);
			double y = Math.abs(this.y - block.y);
			
			if(x == 0){
				this.yBounce();
				Log.d("BALL SPEED", "X = 0, Y TURNED");
			}
			else if(angle < y/x){
				//hit top or bottom edge
				this.yBounce();
				Log.d("BALL SPEED", "topbot, Y TURNED");
	
			}else{
				//hit left or right edge
				this.xBounce(0);
				Log.d("BALL SPEED", "leftright, X TURNED");
	
			}
		}
	}
	
	public void bouncePaddle(Paddle paddle){

		dy = - dy; //reverse y direction
		
		float speed = (float) Math.sqrt(Math.pow(dy, 2)+Math.pow(dx, 2));
		
		dx =  (2* (this.x - paddle.x)/ paddle.width) * (speed) + paddle.dx/100;
		
	}
}
