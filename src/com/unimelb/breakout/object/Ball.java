package com.unimelb.breakout.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.view.WorldView;

public class Ball {
	
	private WorldView worldView;
	private Bitmap bitmap;
	
	//x coordinate
	private volatile float x;
	//y coordinate
	private volatile float y;
	//ball radius
	private float radius;
	
	//x speed
	private volatile float dx;
	//y speed
	private volatile float dy;
		
	private boolean isActive;
	
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
	
	public Ball(WorldView worldView, Bitmap bitmap){
		
		this.worldView = worldView;
		this.bitmap = bitmap;
		this.screenWidth = worldView.width;
		this.screenHeight = worldView.height;
		this.radius = worldView.ball_r;
		this.MIN_X = worldView.wallWidth+radius;
		this.MAX_X = screenWidth - MIN_X;
		this.MIN_Y = worldView.wallWidth+radius;
		this.MAX_Y = screenHeight - MIN_Y;
		
		this.ballPaint = getPaint(Color.RED);
		
		this.x = worldView.initial_x;
		this.y = worldView.initial_y - worldView.paddle_h/2 - radius;
		
		this.dx = 0;
		this.dy = 0;
		//setXspeed(INITIAL_SPEED_X);
		//setYspeed(INITIAL_SPEED_Y);
		
		this.isActive = true;
		
	}

	public void activate(){
		this.isActive = true;
	}
	
	public void deactivate(){
		this.isActive = false;
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

	public float getXspeed() {
		return dx;
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

	public float getYspeed() {
		return dy;
	}

	public void setYspeed(float yspeed) {
		this.dy = yspeed;
	}
	
	public float getRadius(){
		return radius;
	}
	
	public void update(float dt){
		if (isActive) {
	        updatePosition(dt);
	        handleWallCollision(dt);
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
	public void handleWallCollision(float dt){
		
		this.x = this.x + (this.dx * dt);
	    this.y = this.y + (this.dy * dt);
	      
		if(x > MAX_X && dx > 0){
			x = MAX_X;
			this.xBounce(0);
			
		}else if(x < MIN_X && dx < 0){
			x = MIN_X;
			this.xBounce(0);
			
		}else if(y > MAX_Y && dy > 0){
			y = MAX_Y;
			this.yBounce();
			
		}else if(y < MIN_Y && dy < 0){
			y = MIN_Y;
			this.yBounce();
		}
		
	}
	
//	private void updatePhysics() {
//		// TODO Auto-generated method stub
//		if(x>=(this.screenWidth-radius)){
//			this.setX((this.screenWidth-radius));
//			this.xBounce(0);
//		}
//		
//		if(x<=radius){
//			this.setX(radius);
//			this.xBounce(0);
//		}
//		
//		if(y>=(this.screenHeight-radius)){
//			this.setY(this.screenHeight-radius);
//			this.yBounce();
//		}
//		
//		if(y<=radius){
//			this.setY(radius);
//			this.yBounce();
//		}
//		
//	}
	
	public void moveBall(float x, float y) {
		// TODO Auto-generated method stub
		setX(x);
		setY(y);
	}
	

	private void updatePosition(float dt) {
		// TODO Auto-generated method stub

		this.x = this.x + (this.dx * dt);
	    this.y = this.y + (this.dy * dt);
	}
	
	public BallState getNextState(float x, float y, float dx, float dy, float accel, float dt){
		float x2  = x + (dt * dx) + (accel * dt * dt * 0.5f);
		float y2  = y + (dt * dy) + (accel * dt * dt * 0.5f);
		float dx2 = dx + (accel * dt) * (dx > 0 ? 1 : -1);
		float dy2 = dy + (accel * dt) * (dy > 0 ? 1 : -1);
		
		return new BallState(x2,y2,dx2,dy2);
	}
	
	public void accelerate(float x, float y, float dx, float dy, float accel, float dt){
		this.x = x + (dt * dx) + (accel * dt * dt * 0.5f);
		this.y  = y + (dt * dy) + (accel * dt * dt * 0.5f);
		this.dx = dx + (accel * dt) * (dx > 0 ? 1 : -1);
		this.dy = dy + (accel * dt) * (dy > 0 ? 1 : -1);
		
	}
	
	public Point getPosition(){
		return new Point(x, y);
	}
	
	public void yBounce(){
		setYspeed(-dy);
	}

	public void xBounce(float acceleration){
		if(Math.abs(dx) > INITIAL_SPEED_X){
			if(dx >= 0){
				setXspeed(dx - 2);
			}else{
				setXspeed(dx + 2);
			}
		}
		setXspeed(-dx + acceleration);
	}
	
	public void onDraw(Canvas canvas){
		
		update(1);
			
		if(worldView.getOnScreen()){
			
			//updatePosition(x,y);
			canvas.drawCircle(x, y, radius, this.ballPaint);
		}
	}
	
	private Paint getPaint(int color){
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(color);
		return paint;
	}


	public void bounceBlock(Block block){
		float WIDTH = 2*this.getRadius() + block.getWidth();
		float HEIGHT = 2*this.getRadius() + block.getHeight();
		
		float angle = HEIGHT/WIDTH;
		
		float x = this.getX() - (block.getX() + block.getWidth()/2);
		float y = this.getY() - (block.getY() + block.getHeight()/2);
	
		if(x == 0){
			this.yBounce();
		}else if(angle == Math.abs(y/x) ){
			//hit the corner
			this.xBounce(0);
			this.yBounce();
		}else if(angle > Math.abs(y/x)){
			//hit left or right edge
			this.xBounce(0);
		}else{
			//hit top or bottom edge
			this.yBounce();
		}
	}
//	
//	private void HandlePaddleCollision(Paddle paddle) {
//	    if (dy > 0 && paddle.collide(this)) {
//	    	
//	        dy = -dy;
//	        
//	        position.Y = paddle.Bounds.Y - sprite.Height;
//	 
//	        direction.X = ((float)Bounds.Center.X - paddle.Bounds.Center.X) / 
//	                      (paddle.Bounds.Width / 2);
//	        direction = Vector2.Normalize(direction);
//	 
//	        // Increase the speed when the ball is hit
//	        speed += speedIncrement;
//	        speed = Math.Min(speed, maxSpeed);
//	    }
//	}
	
	public void bouncePaddle(Paddle paddle){
		float WIDTH = 2*this.getRadius() + paddle.getWidth();
		float HEIGHT = 2*this.getRadius() + paddle.getHeight();
		
		float angle = HEIGHT/WIDTH;
		
		float x = this.getX() - (paddle.getX() + paddle.getWidth()/2);
		float y = this.getY() - (paddle.getY() + paddle.getHeight()/2);

		dy = - dy; //reverse y direction
		dx =  2* (this.x - paddle.x)/ paddle.width;
		
	}
	
	/**
	 * Internal class for recording state information
	 * @author BinGo
	 *
	 */
	class BallState{
		float x;
		float y;
		float dx;
		float dy;
		
		public BallState(float x, float y, float dx, float dy){
			this.x = x;
			this.y = y;
			this.dx = dx;
			this.dy = dy;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}

		public float getDx() {
			return dx;
		}

		public float getDy() {
			return dy;
		}
		
	}
	
}
