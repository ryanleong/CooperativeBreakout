package com.unimelb.breakout.object;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.unimelb.breakout.R;
import com.unimelb.breakout.enums.GameState;
import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.utils.Utils;
import com.unimelb.breakout.view.WorldView;

public class Ball {
	
	private WorldView worldView;
	private Bitmap bitmap;
	
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
	private Paint ballPaint;
	Drawable d;
	
	public Ball(WorldView worldView, Bitmap bitmap){
		
		this.worldView = worldView;
		this.bitmap = bitmap;
		this.screenWidth = worldView.width;
		this.screenHeight = worldView.height;
		this.r = worldView.ball_r;
		this.MIN_X = worldView.wallWidth+r;
		this.MAX_X = screenWidth - MIN_X;
		this.MIN_Y = worldView.wallWidth+r;
		this.MAX_Y = screenHeight - MIN_Y;
		
		this.ballPaint = getPaint(Color.RED);
		d = worldView.getResources().getDrawable(R.drawable.ball_blue);

		
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
	
	public void update(float dt){
		if (y >= screenHeight){
			end();
		}
		
		if (isActive()) {
			
	        updatePosition(dt);
	        handleWallCollision();
	        
		}
//	        if (position.Y > screenHeight) {
//	            state = State.Dead;
//	        } else {
//	            HandleCollisions();
//	        }
//	    } else if (!isActive && 
//	           Keyboard.GetState().IsKeyDown(Keys.Space)) {
//	        LaunchBall();
//	    }
	}
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
//		else if(y > MAX_Y && dy > 0){
//			y = MAX_Y;
//			this.yBounce();
//			
//		}
		
	}

	private void updatePosition(float dt) {
		// TODO Auto-generated method stub
		
		this.x = x + (dt * dx) + (da * dt * dt * 0.5f);
		this.y  = y + (dt * dy) + (da * dt * dt * 0.5f);
		this.dx = dx + (da * dt) * (dx > 0 ? 1 : -1);
		this.dy = dy + (da * dt) * (dy > 0 ? 1 : -1);
	}
	
//	public void accelerate(float x, float y, float dx, float dy, float accel, float dt){
//		this.x = x + (dt * dx) + (accel * dt * dt * 0.5f);
//		this.y  = y + (dt * dy) + (accel * dt * dt * 0.5f);
//		this.dx = dx + (accel * dt) * (dx > 0 ? 1 : -1);
//		this.dy = dy + (accel * dt) * (dy > 0 ? 1 : -1);
//		
//	}
	
	public void yBounce(){
		dy = -dy;
	}

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
	
	private Paint getPaint(int color){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		return paint;
	}


	public void bounceBlock(Block block){
		
		float WIDTH = 2*r + block.width;
		float HEIGHT = 2*this.r + block.height;
		
		double angle = HEIGHT/WIDTH;
		
		double x = Math.abs(this.x - block.x);
		double y = Math.abs(this.y - block.y);
		
		if(x == 0){
			this.yBounce();
		}
//		else if(angle == y/x ){
//			//hit the corner
//			this.xBounce(0);
//			this.yBounce();
//		}
		else if(angle > y/x){
			//hit left or right edge
			this.xBounce(0);
		}else{
			//hit top or bottom edge
			this.yBounce();
		}
	}
	
	public void bouncePaddle(Paddle paddle){

		dy = - dy; //reverse y direction
		
		float speed = (float) Math.sqrt(Math.pow(dy, 2)+Math.pow(dx, 2));
		
		dx =  (2* (this.x - paddle.x)/ paddle.width) * (speed) + paddle.dx/100;
		
//		dy = -dy;
//		dx = -dx + paddle.dx/100;
		
	}
	
//	public boolean handleCollision(Block block){
//		Point intersect;
//		boolean collide = false;;
//		Point block_left_top = new Point(block.x - block.width/2, block.y - block.height/2);
//		Point block_right_top = new Point(block.x + block.width/2, block.y - block.height/2);
//		Point block_left_bot = new Point(block.x - block.width/2, block.y + block.height/2);
//		Point block_right_bot = new Point(block.x + block.width/2, block.y + block.height/2);
//		
//		float last_x = this.last_x;
//		float last_y = this.last_x;
//		float x = this.x;
//		float y = this.y;
//		
//		if(last_x < x){
//			last_x -= r;
//			x += r;
//		}else{
//			last_x += r;
//			x -= r;
//		}
//		
//		if(last_y < y){
//			last_y -= r;
//			y += r;
//		}else{
//			last_y += r;
//			y -= r;
//		}
//		
//		Point from = new Point(last_x, last_y);
//		Point to = new Point(x, y);
//		
//		//collide left edge of the block
//		intersect = Utils.intersect(from, to, block_left_top, block_left_bot);
//		if(intersect != null){
//			x = intersect.x - r;
//			y = intersect.y;
//			dx = -dx;
//			collide=true;
//		}
//		
//		//collide right edge of the block
//		intersect = Utils.intersect(from, to, block_right_top, block_right_bot);
//		if(intersect != null){
//			x = intersect.x + r;
//			y = intersect.y;
//
//			dx = -dx;
//			collide=true;
//
//		}
//		
//		//collide top edge of the block
//		intersect = Utils.intersect(from, to, block_left_top, block_right_top);
//		if(intersect != null){
//			x = intersect.x;
//
//			y = intersect.y - r;
//			dy = -dy;
//			collide=true;
//
//		}
//		
//		//collide bottom edge of the block
//		intersect = Utils.intersect(from, to, block_left_bot, block_right_bot);
//		if(intersect != null){
//			x = intersect.x;
//
//			y = intersect.y + r;
//			dy = -dy;
//			collide=true;
//
//		}
//		
//		return collide;
//		
//	}
//	
//	public void handleBlockCollision(ArrayList<Block> blocks){
//		Iterator<Block> list = blocks.iterator();
//		//iterate each block
//		ArrayList<Block> collideBlocks = new ArrayList<Block>();
//		
//		while(list.hasNext()){
//			Block b = list.next();
//			//if collide then remove the block
//			Point left_top = new Point(b.left_edge, b.top_edge);
//			Point left_bot = new Point(b.left_edge, b.bottom_edge);
//			Point right_top = new Point(b.right_edge, b.top_edge);
//			Point right_bot = new Point(b.right_edge, b.bottom_edge);
//			
//			Point from = new Point(x,y);
//			update(1);
//			Point to = new Point(x,y);
//			
//			if()
//			
//			Point intersect = Utils.intersect(x, y, b1, b2)
//			if(this.hasCollision(ball, b)){
//			//if(ball.handleCollision(b)){
//				list.remove();
//				if(!ballReacted){
//					ball.bounceBlock(b);
//					ballReacted = true;
//				}
//				this.blockRemoved();
//			}else{
//				b.onDraw(canvas);
//			}
//		}	
//	}
}
