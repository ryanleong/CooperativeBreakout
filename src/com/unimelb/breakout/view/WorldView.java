package com.unimelb.breakout.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import com.unimelb.breakout.R;
import com.unimelb.breakout.activity.MainActivity;
import com.unimelb.breakout.object.Ball;
import com.unimelb.breakout.object.Block;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.Paddle;
import com.unimelb.breakout.utils.LocalMapUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

/**
 * This class implements the game screen of challenge mode.
 * For challenge mode, the rank and next fields are not available. 
 * However, the player can enter the next level if the current level is clear.
 * 
 */
public class WorldView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
	
	protected SurfaceHolder surfaceHolder;
	public Activity activity;
	Drawable d;
	
	//Game Control flags
	protected boolean isRunning = false;
	protected boolean onScreen = true;
	protected boolean isBallLaunched = false;
	protected boolean touchOnPaddle = false;
	//private boolean connected = false;
	//private OutputStream outputStream;
	
	//Game Objects
	public Ball ball;
	public int ball_r = 30;
	public ArrayList<Block> blocks = null;
	public Paddle paddle;
	public float paddle_w = 300;
	public float paddle_h = 50;
	float blockWidth;
	float blockHeight;

	//Game Configurations
	public int padding = 10;
	public int reward = 5;
	public int width;
	public int height;
	public int wallWidth = 0;
	public int ballRadius = 30;
	public float initial_x;
	public float initial_y;
	public float initialBallXSpeed = 0;
	public float initialBallYSpeed = 20;
	
	public int lives = 3;
	
	protected onBlockRemoveListener blockRemoveListener;
	
	protected onLifeLostListener lifeLostListener;

	protected onGameOverListener gameOverListener;
	
	protected onGameClearListener gameClearListener;
	
	//listener of motion velocity
	private VelocityTracker mVelocityTracker = null;
	
	Thread thread;
	Map mMap;

	public WorldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.getHolder().addCallback(this);
		this.setFocusable(true);	
		
	}

	//Called when the surfaceView first created
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		d = activity.getResources().getDrawable(R.drawable.game_bg);

		// TODO Auto-generated method stub
		this.surfaceHolder = holder;
		this.isRunning = true;
		
		this.width = getWidth();
		this.height = getHeight();
		
		this.initialise();
		
		thread = new Thread(this);
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
				
				canvas = surfaceHolder.lockCanvas(null);
				if(canvas!= null){

					synchronized(surfaceHolder){
						onDraw(canvas);

						if(!ball.isEnd()){
							if(!blocks.isEmpty()){
								boolean hasCollided = false;
								//stage not clear
								ListIterator<Block> list = blocks.listIterator(blocks.size());
								//iterate blocks in reverse order, that is, start with the bottom block first
								while(list.hasPrevious()){
									Block b = list.previous();
									//if collide then remove the block
									if(!hasCollided && this.hasCollision(ball, b)){
									//if(ball.handleCollision(b)){
										hitEffect(b);
										list.remove();
										ball.bounceBlock(b);
										this.blockRemoved();
										hasCollided = true;

									}else{
										b.onDraw(canvas);
									}
								}
								
								//this.handleBlockCollision(blocks, ball, canvas);
							}else{
								//notify that the stage is clear.
								gameclear();
								break;
							}
						}else{
							
							Log.i("BLOCKS", "size: " + blocks.size());
							this.lifeLost();
							if(lives > 0){
								this.start();
							}else{
								this.gameover();
								break;
							}			
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
	
	public void chanllengeModeLogic(){
		
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		//canvas.drawColor(Color.BLUE);
		d.setBounds(0, 0, width, height);
		d.draw(canvas);
	}
	
	public void start(){
		this.isBallLaunched = false;
		ball.ready();
		ball.x = this.initial_x;
		ball.y = this.initial_y - this.paddle_h/2 - ball.r;
		
		paddle.x = this.initial_x;
		paddle.y = this.initial_y;
	}
	
	public void initialise(){
		try {
			mMap = LocalMapUtils.getMap(((MainActivity) activity).getMap(), activity);
//			this.initial_x = width/2;
//			this.initial_y = height/6*5;
			this.initial_x = ((float)mMap.getInitialX()/100)*width;
			this.initial_y = ((float)mMap.getInitialY()/100)*height;
			this.initialBallXSpeed =  ((float)mMap.getBallInitialXSpeed()/100)*width;
			this.initialBallYSpeed =  ((float)mMap.getBallInitialYSpeed()/100)*height;
			this.paddle_w = ((float)mMap.getPaddleWidth()/100)*width;
			this.paddle_h = ((float)mMap.getPaddleHeight()/100)*height;
			this.blocks = this.generateBlocks(mMap, width, height);
			this.paddle = new Paddle(this, null);
			this.ball = new Ball(this, null);
			start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isRunning = false;
			((MainActivity)activity).showDialog("File Not Found","Cannot find the selected map.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			((MainActivity)activity).showDialog("File Exception","Cannot open the selected map.");
		}

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
		this.ball.dy = this.initialBallYSpeed;
		this.ball.dx = this.initialBallXSpeed;
	}
	
	/**
	 * Stop the game
	 */
	public void stopRunning(){
		this.isRunning = false;
		this.onScreen = false;
		this.isBallLaunched = false;
	}
	
	public void restart(){
		this.initialise();
		this.isRunning = true;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public Paint getDashLinePaint(){
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255 , 255);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(10);
		paint.setPathEffect(new DashPathEffect(new float[] {30,50}, 0));
		return paint;
	}
	
	/**
	 * Generate the blocks according to the given map.
	 * 
	 * @param screenWidth
	 * @param screenHeight
	 * @param level
	 * @return
	 */
	public ArrayList<Block> generateBlocks(Map mMap, int screenWidth, int screenHeight){
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		int map[][] = mMap.getMap();
		

		blockWidth = (screenWidth-2*padding)*((float)mMap.getBlockWidth()/100);
		blockHeight = (screenHeight-2*padding)*((float)mMap.getBlockHeight()/100);
		
		for(int i = 0; i < mMap.getColumn(); i++){
			for(int j = 0; j < mMap.getRow(); j++){
			
				if(map[i][j]!=0){
					//blocks.add(new Block(this, null, padding+(i%8)*blockWidth, padding+(i%5)*blockHeight, blockWidth/2, blockHeight/2, i));
	
					blocks.add(new Block(this, null, padding+blockWidth/2+j*blockWidth, padding+blockHeight/2+ i*+blockHeight, blockWidth, blockHeight, map[i][j]));
				
				}
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
//		Point p = block.getNearestPoint(ball.x, ball.y);
//		Vector v_p = new Vector(p.x - block.x, p.y - block.y);
//		
//		Vector v = new Vector(ball.x - block.x, ball.y - block.y);
//		Vector v_u = v.getUnitVector();
//		
//		float projection = v_p.dotProduct(v_u);
//		
//		if (v.magnitude- Math.abs(projection) - ball.r > 0 && v.magnitude > 0){
//			return false;
//		}
//		
//		return true;

		//Axis-Aligned Bounding Box Algorithm
		float ball_left = ball.x - ball.r;
		float ball_right = ball.x + ball.r;
		float ball_top = ball.y - ball.r;
		float ball_bottom = ball.y + ball.r;
		
		float block_left = block.x - block.width/2;
		float block_right = block.x + block.width/2;
		float block_top = block.y- block.height/2;
		float block_bottom = block.y + block.height/2;

		return (ball_left <= block_right
				&& block_left <= ball_right
				&& ball_top <= block_bottom
				&& block_top <= ball_bottom);
	}
	
	public void hitEffect(Block block){
		switch(block.getType()){
			case 1:
				//normal block, no effect.
				break;
			case 2:
				//special block, add life
				lifeBonus();
				break;
			case 3:
				//special block, enlarge paddle
				enlargePaddle();
				break;
		}
	}
	/**
	 * Monitor the finger motion and move paddle according to the motion event
	 */
	@SuppressLint({ "ClickableViewAccessibility", "Recycle" }) @Override
	public boolean onTouchEvent (MotionEvent event){
		int index = event.getActionIndex();
        int pointerId = event.getPointerId(index);
        
		float px = 0;
		
		float x_diff = 0;
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
				//py = event.getY();
				

						
				if(this.paddle.isXCovered(px)){
					x_diff = paddle.x - px;
					touchOnPaddle = true;
				}else{
					touchOnPaddle = false;
				}
               
				break;
			case MotionEvent.ACTION_MOVE:
				px = event.getX();
				
				 mVelocityTracker.addMovement(event);
	             mVelocityTracker.computeCurrentVelocity(1000);
	             
	             this.paddle.dx = VelocityTrackerCompat.getXVelocity(mVelocityTracker, pointerId);
	             
				if(this.paddle.isXCovered(px)){
					
					this.paddle.x = px + x_diff;
					touchOnPaddle = true;
				}else{
					touchOnPaddle = false;
				}
				
				if(!isBallLaunched && touchOnPaddle){
					ball.x = px + x_diff;
				}
				
				break;
			case MotionEvent.ACTION_UP:
				mVelocityTracker = null;
				
				if(!isBallLaunched && touchOnPaddle){
					this.launchBall();
				}
				
				break;
			case MotionEvent.ACTION_CANCEL:
				mVelocityTracker = null;
				break;
			
		}
		return true;
		
	}
	
	public void setActivity(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
	}
	
	public void enlargePaddle(){
		this.paddle.width += width/10;
		this.paddle.update();
	}
	
	/**
	 * Interfaces that used to notify the activity that hosting this view an event
	 */
	
	/**
	 * This interface must be implemented by the activity that contains
	 * this view to enable the communication between this view and its
	 * host activity.
	 * 
	 * This listener notifies the activity that a block is being removed.
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
			blockRemoveListener.onBlockRemoved(reward);
		}
	}
	
	/**
	 * This interface must be implemented by the activity that contains
	 * this view to enable the communication between this view and its
	 * host activity.
	 *
	 * This listener notifies the activity that the user lost one life.
	 * 
	 */
	public interface onLifeLostListener{
		void onLifeLost(int lives);
	}
	
	public void setOnLifeLostListener(onLifeLostListener l) {
		lifeLostListener = l;
	}
	
	public void lifeLost(){
		Log.i("Lives", "Lives: " + lives);
		lives -= 1;
		if(lifeLostListener != null){
			lifeLostListener.onLifeLost(lives);
		}
	}
	
	public void lifeBonus(){
		Log.i("Lives", "Lives: " + lives);
		lives += 1;
		if(lifeLostListener != null){
			lifeLostListener.onLifeLost(lives);
		}
	}
	
	/**
	 * This interface must be implemented by the activity that contains
	 * this view to enable the communication between this view and its
	 * host activity.
	 *
	 * This listener notifies the activity that the game is over.
	 * 
	 */
	public interface onGameOverListener{
		void onGameOver();
	}
	
	public void setOnGameOverListener(onGameOverListener l) {
		gameOverListener = l;
	}
	
	public void gameover(){
		Log.i("STATUS", "Game over!");
		isRunning = false;
		thread = null;
		if(gameOverListener != null){
			gameOverListener.onGameOver();
		}
	}


	
	/**
	 * This interface must be implemented by the activity that contains
	 * this view to enable the communication between this view and its
	 * host activity.
	 *
	 * This listener notifies the activity that the stage is clear.
	 * 
	 */
	public interface onGameClearListener{
		void onGameClear();
	}
	
	public void setOnGameClearListener(onGameClearListener l) {
		gameClearListener = l;
	}
	
	public void gameclear(){
		Log.i("STATUS", "Game clear!");
		isRunning = false;
		thread = null;
		if(gameClearListener != null){
			gameClearListener.onGameClear();
		}
	}

	
}
