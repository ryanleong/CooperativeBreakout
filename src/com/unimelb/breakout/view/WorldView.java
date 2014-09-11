package com.unimelb.breakout.view;

import java.util.ArrayList;
import java.util.Iterator;

import com.unimelb.breakout.object.Ball;
import com.unimelb.breakout.object.Block;
import com.unimelb.breakout.object.Maps;
import com.unimelb.breakout.object.Paddle;
import com.unimelb.breakout.utils.Point;
import com.unimelb.breakout.utils.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

/**
 * TODO: document your custom view class.
 */
public class WorldView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	
	private SurfaceHolder surfaceHolder;
	
	//Game Control flags
	private boolean isRunning = false;
	private boolean onScreen = true;
	private boolean isBallLaunched = false;
	//private boolean connected = false;
	//private OutputStream outputStream;
	
	//Game Objects
	public Ball ball;
	public int ball_r = 30;
	public ArrayList<Block> blocks = null;
	public Paddle paddle;
	public int paddle_w = 300;
	public int paddle_h = 50;

	//Game Configurations
	public int width;
	public int height;
	public int wallWidth = 0;
	public int ballRadius = 30;
	public int initial_x;
	public int initial_y;
	
	private onBlockRemoveListener listener;
	
	//listener of motion velocity
	private VelocityTracker mVelocityTracker = null;

	public WorldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.getHolder().addCallback(this);
		this.setFocusable(true);	
		
	}

	//Called when the surfaceView first created
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		this.surfaceHolder = holder;
		this.isRunning = true;
		
		this.width = getWidth();
		this.height = getHeight();
		this.initial_x = width/2;
		this.initial_y = height/6*5;
		
		this.blocks = this.generateBlocks(width, height, 1);
		this.paddle = new Paddle(this, null);
		this.ball = new Ball(this, null);
		
		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@SuppressLint("WrongCall") @Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			Canvas canvas = null;
			try{
				canvas = surfaceHolder.lockCanvas(null);
				synchronized(surfaceHolder){
					onDraw(canvas);
					
					if(!blocks.isEmpty()){
						Iterator<Block> list = blocks.iterator();
						while(list.hasNext()){
							Block b = list.next();
							if(this.hasCollision(ball, b)){
								list.remove();
								ball.bounceBlock(b);
								this.blockRemoved();
							}else{
								b.onDraw(canvas);
							}
						}
					}
					if(isBallLaunched){
						if(this.hasCollision(ball, paddle)){
							ball.bouncePaddle(paddle);
						}
					}
					ball.onDraw(canvas);
					paddle.onDraw(canvas);
				}
			}finally{
				if(canvas!=null){
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
//			try{
//				Thread.sleep(10);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLUE);
	}
	
	public boolean getOnScreen(){
		return this.onScreen;
	}
	
	private void launchBall(){
		this.isBallLaunched = true;
		this.ball.setYspeed(-5);
	}
	
	public void stopRunning(){
		this.isRunning = false;
		this.onScreen = false;
		this.isBallLaunched = false;
	}
	
	public ArrayList<Block> generateBlocks(int screenWidth, int screenHeight, int level){
		ArrayList<Block> blocks = new ArrayList<Block>();
		int map[] = Maps.getMap(level);
		
		int padding = 10;
		float blockWidth = (float)(screenWidth-2*padding)/8;
		float blockHeight = (float)(screenHeight-2*padding)/10;
		
		for(int i = 0; i < map.length; i++){
			if(map[i]==1){
				blocks.add(new Block(this, null, padding+(i%8)*blockWidth, padding+(i%5)*blockHeight, blockWidth/2, blockHeight/2));
			}
		}
		
		return blocks;
	}
	
	/**
	 * Collision Detection Method
	 * Using Separating Axis Theorem
	 * @param ball
	 * @param block
	 * @return
	 */
	public boolean hasCollision(Ball ball, Block block){

		Point p = block.getNearestPoint(ball.getX(), ball.getY());
		Vector v_p = new Vector(p.x - block.getX(), p.y - block.getY());
		
		Vector v = new Vector(ball.getX() - block.getX(), ball.getY() - block.getY());
		Vector v_u = v.getUnitVector();
		
		float projection = v_p.dotProduct(v_u);
		
		if (v.magnitude- Math.abs(projection) - ball.getRadius() > 0 && v.magnitude > 0){
			return false;
		}else{
			return true;
		}
	}
	
//	public boolean hasCollidedBlock(Ball ball, Block block) {
//		float x1 = ball.getX() - ball.getRadius();  
//		float y1 = ball.getY() - ball.getRadius();  
//		float x2 = ball.getX() + ball.getRadius();  
//		float y2 = ball.getY() - ball.getRadius();  
//		float x3 = ball.getX() + ball.getRadius();  
//		float y3 = ball.getY() + ball.getRadius();  
//		float x4 = ball.getX() - ball.getRadius();  
//		float y4 = ball.getY() + ball.getRadius();  
//		
//		boolean collision = false;
//		
//		if(block.isCovered(x1, y1) || block.isCovered(x2, y2) ||
//		   block.isCovered(x3, y3) || block.isCovered(x4, y4)){
//			return true;
//		}
//		
//		return false;
//	}
//	
//	public boolean hasCollidedPaddle(Ball ball, Paddle paddle) {
//		float x1 = ball.getX() - ball.getRadius();  
//		float y1 = ball.getY() - ball.getRadius();  
//		float x2 = ball.getX() + ball.getRadius();  
//		float y2 = ball.getY() - ball.getRadius();  
//		float x3 = ball.getX() + ball.getRadius();  
//		float y3 = ball.getY() + ball.getRadius();  
//		float x4 = ball.getX() - ball.getRadius();  
//		float y4 = ball.getY() + ball.getRadius();  
//		
//		if(paddle.isCovered(x1, y1) || paddle.isCovered(x2, y2) ||
//		   paddle.isCovered(x3, y3) || paddle.isCovered(x4, y4)){
//			return true;
//		}
//		
//		return false;
//
//	}


	

	@Override
	public boolean onTouchEvent (MotionEvent event){
		int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
        
		float px = 0;
		float py = 0;
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				
				if(mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                }
                else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                
				px = event.getX();
				py = event.getY();
				
				if(!isBallLaunched && this.paddle.isXCovered(px)){
					this.ball.setX(px);
				}
//                
				break;
			case MotionEvent.ACTION_MOVE:
				float mx = event.getX();
				//float my = event.getY();
				mVelocityTracker.addMovement(event); 
                mVelocityTracker.computeCurrentVelocity(1000);
//                this.paddle.setXspeed(VelocityTrackerCompat.getXVelocity(mVelocityTracker, 
//                        pointerId));
//                
				if(this.paddle.isXCovered(mx)){
					float d = this.paddle.getWidth()/2 + 2*ball.getRadius();
					if(hasCollision(this.ball, this.paddle) && this.paddle.collisionOnLeft(ball)){
						if(px < d){
							px = d;
						}
					}else if(hasCollision(this.ball, this.paddle) && this.paddle.collisionOnRight(ball)){
						if(px > (width - d)){
							px = width - d;
						}
					}
					
					//this.paddle.movePaddle(px, 0);
					this.paddle.setX(mx);
					//this.paddle.x += (mx - px);
					//px = mx;
				}
				
				if(!isBallLaunched){
					ball.setX(mx);
				}
				
				break;
			case MotionEvent.ACTION_UP:
				mVelocityTracker = null;
				
				if(!isBallLaunched){
					this.launchBall();
				}
				
				break;
			case MotionEvent.ACTION_CANCEL:
				mVelocityTracker = null;
				break;
			
		}
		return true;
		
	}
	
	/**
	 * Interfaces that used to notify the activity that hosting this view an event
	 */
	
	/**
	 * This interface must be implemented by the activity that contains
	 * this view to enable the communication between this view and its
	 * host activity.
	 *
	 */
	public interface onBlockRemoveListener{
		void onBlockRemoved(int socre);
	}
	
	public void setOnBlockRemoveListener(onBlockRemoveListener l) {
        listener = l;
	}
	
	public void blockRemoved(){
		if(listener != null){
			listener.onBlockRemoved(5);
		}
	}
	
}
