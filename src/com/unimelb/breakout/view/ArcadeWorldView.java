package com.unimelb.breakout.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ListIterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

import com.unimelb.breakout.activity.MainActivity;
import com.unimelb.breakout.object.Ball;
import com.unimelb.breakout.object.Block;
import com.unimelb.breakout.object.Paddle;
import com.unimelb.breakout.utils.LocalMapUtils;

/**
 * This class implements the game screen of arcade mode.
 * For arcade mode, the rank and next fields are available. 
 * Player can submit any high score to the server to get to be ranked.
 * There is only one level for arcade mode and this level will never be
 * cleared because of infinite blocks.
 * 
 */
public class ArcadeWorldView extends WorldView implements SurfaceHolder.Callback, Runnable{

	private Paint dashLinePaint;
	private float dashLinePos;
	private int collisionCount;
	private int threshold = 5;
	private boolean isPause = false;
	
	private onDifficultyIncreaseListener difficultyIncreaseListener;
	
	public ArcadeWorldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.getHolder().addCallback(this);
		this.setFocusable(true);			
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		d.setBounds(0, 0, width, height);
		d.draw(canvas);
		canvas.drawLine(0, dashLinePos, width, dashLinePos, dashLinePaint);
	}
	
	/**
	 * Main loop that running the game
	 */
	@SuppressLint("WrongCall") @Override
	public void run() {
		// TODO Auto-generated method stub
		while(isRunning){
			int i = 0;
			while(!isPause){
				Canvas canvas = null;
	
				try{
					
					canvas = surfaceHolder.lockCanvas(null);
					if(canvas!= null){
	
						synchronized(surfaceHolder){
							
							onDraw(canvas);
	
							if(!ball.isEnd()){
								if(!blocks.isEmpty()){
									if(collisionCount < threshold){
										boolean hasCollided = false;
										i++;
										//stage not clear
										ListIterator<Block> list = blocks.listIterator(blocks.size());
										//iterate blocks in reverse order, that is, start with the bottom block first
										while(list.hasPrevious()){
											Block b = list.previous();
											if(b.bottom_edge < dashLinePos){
												//if collide then remove the block
												if(!hasCollided && this.hasCollision(ball, b)){
													hitEffect(b);
													list.remove();
													ball.bounceBlock(b);												
													Log.d("LOOP", "one block removed");
													this.blockRemoved();	
													hasCollided = true;
													//Log.d("LOOP TURNED", hasCollided+"");
													Log.d("BALL SPEED", ball.dy+"");
													Log.d("BALL POS", ball.y+"");
													Log.d("LOOPED", i+"");
												}else{
													b.onDraw(canvas);
												}
											}else{
												//if any blocks reach dash line, the game terminates immediately
												gameover();
												break;
											}		
											
										}
									}else{
										/*
										 * number of blocks reaches threshold, then move all blocks down a row and
										 * then add a new row of blocks
										 */
										moveDownAndAddBlocks();
										
									}
								}else{
									this.difficultyIncrease();
								}
								
							}else{
								
								Log.i("BLOCKS", "size: " + blocks.size());
								this.lifeLost();
								if(lives > 0){
									this.start();	
								}else{
									this.gameover();								
								}			
							}
							//enable collision detection after the ball is launched
							if(isBallLaunched){
								if(this.hasCollision(ball, paddle)){
									ball.bouncePaddle(paddle);
									collisionCount ++;
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
			}
		}		
	}
	
	@Override
	public void initialise(){
		try {
			mMap = LocalMapUtils.getMap(((MainActivity) activity).getMap(), activity);
			this.initial_x = ((float)mMap.getInitialX()/100)*width;
			this.initial_y = ((float)mMap.getInitialY()/100)*height;
			this.paddle_w = ((float)mMap.getPaddleWidth()/100)*width;
			this.paddle_max_w = paddle_w*2;
			this.paddle_h = ((float)mMap.getPaddleHeight()/100)*height;
			this.initial_y = ((float)mMap.getInitialY()/100)*height;
			this.initialBallXSpeed =  ((float)mMap.getBallInitialXSpeed()/100)*width;
			this.initialBallYSpeed =  ((float)mMap.getBallInitialYSpeed()/100)*height;
			dashLinePaint = getDashLinePaint();
			dashLinePos = initial_y - height/10;
			this.blocks = this.generateBlocks(mMap, width, height);
			this.collisionCount = 0;
			this.paddle = new Paddle(this, null);
			this.ball = new Ball(this, null);
			start();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isRunning = false;
			isPause = true;
			((MainActivity)activity).showDialog("File Not Found","Cannot find the selected map.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			((MainActivity)activity).showDialog("File Exception","Cannot open the selected map.");
		}

	}
		
	public Paint getDashLinePaint(){
		Paint paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setStrokeWidth(10);
		paint.setStyle(Style.STROKE);
		paint.setPathEffect(new DashPathEffect(new float[] {30,50}, 0));
		return paint;
	}
	
	public void moveDownAndAddBlocks(){
		//iterate each block
		
		Log.d("MOVEDOWN", "run once");
		for(Block b : blocks){

			b.moveDown();

		}
		
	    for(int i = 0; i < mMap.getColumn(); i++){	    
	    		double random = Math.random();
	    		if (random > 0.9){
		    		blocks.add(new Block(this, null, padding+blockWidth/2+i*blockWidth, padding+blockHeight/2, blockWidth, blockHeight, 2));
	    		}else{
	    			blocks.add(new Block(this, null, padding+blockWidth/2+i*blockWidth, padding+blockHeight/2, blockWidth, blockHeight, 1));
	    		}
	    }
	    
	    this.collisionCount = 0;
	}	
	
	/**
	 * This interface must be implemented by the activity that contains
	 * this view to enable the communication between this view and its
	 * host activity.
	 *
	 * This listener notifies the activity that the stage is clear.
	 * 
	 */
	public interface onDifficultyIncreaseListener{
		void onDifficultyIncrease();
	}
	
	public void setOnDifficultyIncreaseListener(onDifficultyIncreaseListener l) {
		difficultyIncreaseListener = l;
	}
	
	public void difficultyIncrease(){
		isPause = true;
		if(difficultyIncreaseListener != null){
			difficultyIncreaseListener.onDifficultyIncrease();
		}
	}
	
	public void increaseDiff(){
		this.blocks = this.generateBlocks(mMap,width, height);
		//increase the frequency of adding new blocks
		this.threshold --;
		this.collisionCount = 0;
		//increase the reward of removing block
		this.reward = 10;
		this.isPause = false;
		start();
	}
	
	@Override
	public void gameover(){
		Log.i("STATUS", "Game over!");
		isRunning = false;
		isPause = true;
		thread = null;
		if(gameOverListener != null){
			gameOverListener.onGameOver();
		}
	}
}

