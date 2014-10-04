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
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	public int lives = 3;
	
	private onBlockRemoveListener blockRemoveListener;
	
	private onLifeLostListener lifeLostListener;

	
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
		
		this.start();
		
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

	/**
	 * Main loop that running the game
	 */
	@SuppressLint("WrongCall") @Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			Canvas canvas = null;
			try{
				
				if(lives > 0){
					canvas = surfaceHolder.lockCanvas(null);
					synchronized(surfaceHolder){
						onDraw(canvas);
						
						if(!ball.isEnd()){
							if(!blocks.isEmpty()){
								//stage not clear
								Iterator<Block> list = blocks.iterator();
								//iterate each block
								while(list.hasNext()){
									Block b = list.next();
									//if collide then remove the block
									if(this.hasCollision(ball, b)){
									//if(ball.handleCollision(b)){
										list.remove();
										ball.bounceBlock(b);
										this.blockRemoved();
									}else{
										b.onDraw(canvas);
									}
								}
							}else{
								//notify that the stage is clear.
							}
						}else{
							this.start();
						}
						//enable collision detection after the ball is launched
						if(isBallLaunched){
							if(this.hasCollision(ball, paddle)){
								ball.bouncePaddle(paddle);
							}
						}
						
						ball.onDraw(canvas);
						paddle.onDraw(canvas);
					}
				}else{				
					gameover();					
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
	
	public void gameover(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
		dialog.setTitle("Game Over");
		dialog.setMessage("Do you want to play again?");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		dialog.setPositiveButton("No", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void start(){
		this.isBallLaunched = false;
		this.blocks = this.generateBlocks(width, height, 1);
		this.paddle = new Paddle(this, null);
		this.ball = new Ball(this, null);
	}
	
	public boolean getOnScreen(){
		return this.onScreen;
	}
	
	/**
	 * Launch the ball
	 */
	private void launchBall(){
		this.isBallLaunched = true;
		this.ball.activate();
		this.ball.dy = 2;
	}
	
	/**
	 * Stop the game
	 */
	public void stopRunning(){
		this.isRunning = false;
		this.onScreen = false;
		this.isBallLaunched = false;
	}
	
	/**
	 * Generate the blocks according to the given map.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param level
	 * @return
	 */
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
	 * Using Separating Axis Theorem OR Axis-Aligned Bounding Box Algorithm
	 * @param ball
	 * @param block
	 * @return
	 */
	public boolean hasCollision(Ball ball, Block block){

		//Separating Axis Theorem
		Point p = block.getNearestPoint(ball.x, ball.y);
		Vector v_p = new Vector(p.x - block.x, p.y - block.y);
		
		Vector v = new Vector(ball.x - block.x, ball.y - block.y);
		Vector v_u = v.getUnitVector();
		
		float projection = v_p.dotProduct(v_u);
		
		if (v.magnitude- Math.abs(projection) - ball.r > 0 && v.magnitude > 0){
			return false;
		}
		
		return true;

		//Axis-Aligned Bounding Box Algorithm
//		float ball_left = ball.x - ball.r;
//		float ball_right = ball.x + ball.r;
//		float ball_top = ball.y - ball.r;
//		float ball_bottom = ball.y + ball.r;
//		
//		float block_left = block.x - block.width/2;
//		float block_right = block.x + block.width/2;
//		float block_top = block.y- block.height/2;
//		float block_bottom = block.y + block.height/2;
//
//		return (ball_left <= block_right
//				&& block_left <= ball_right
//				&& ball_top <= block_bottom
//				&& block_top <= ball_bottom);
	}

	/**
	 * Monitor the finger motion and move paddle according to the motion event
	 */
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent (MotionEvent event){
		int index = event.getActionIndex();
//        int action = event.getActionMasked();
//        int pointerId = event.getPointerId(index);
        
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
					this.ball.x = px;
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
					float d = this.paddle.width/2 + 2*ball.r;
//					if(hasCollision(this.ball, this.paddle) && this.paddle.collisionOnLeft(ball)){
//						if(px < d){
//							px = d;
//						}
//					}else if(hasCollision(this.ball, this.paddle) && this.paddle.collisionOnRight(ball)){
//						if(px > (width - d)){
//							px = width - d;
//						}
//					}
					
					//this.paddle.movePaddle(px, 0);
					this.paddle.x = mx;
					//this.paddle.x += (mx - px);
					//px = mx;
				}
				
				if(!isBallLaunched){
					ball.x = mx;
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
        blockRemoveListener = l;
	}
	
	public void blockRemoved(){
		if(blockRemoveListener != null){
			blockRemoveListener.onBlockRemoved(5);
		}
	}
	
	public interface onLifeLostListener{
		void onLifeLost();
	}
	
	public void setOnLifeLostListener(onLifeLostListener l) {
		lifeLostListener = l;
	}
	
	public void lifeLost(){
		if(lifeLostListener != null){
			lifeLostListener.onLifeLost();
		}
	}
	
	
}
