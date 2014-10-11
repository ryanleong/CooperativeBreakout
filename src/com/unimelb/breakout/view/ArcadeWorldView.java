package com.unimelb.breakout.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.unimelb.breakout.activity.MainActivity;
import com.unimelb.breakout.object.Ball;
import com.unimelb.breakout.object.Block;
import com.unimelb.breakout.object.Map;
import com.unimelb.breakout.object.Paddle;
import com.unimelb.breakout.utils.LocalMapUtils;
import com.unimelb.breakout.view.WorldView.onBlockRemoveListener;
import com.unimelb.breakout.view.WorldView.onGameClearListener;
import com.unimelb.breakout.view.WorldView.onGameOverListener;
import com.unimelb.breakout.view.WorldView.onLifeLostListener;

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
	private int threshold;
	
	public ArcadeWorldView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.getHolder().addCallback(this);
		this.setFocusable(true);			
	}
	
	@Override
	protected void onDraw(Canvas canvas){
		canvas.drawColor(Color.BLUE);
		canvas.drawLine(0, dashLinePos, width, dashLinePos, dashLinePaint);
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
						boolean ballReacted = false;

						if(!ball.isEnd()){
							
							if(threshold < 5){
								//stage not clear
								Iterator<Block> list = blocks.iterator();
								//iterate each block
								while(list.hasNext()){
									Block b = list.next();
									
									if(b.y < dashLinePos){
										//if collide then remove the block
										if(this.hasCollision(ball, b)){
										//if(ball.handleCollision(b)){
											list.remove();
											if(!ballReacted){
												ball.bounceBlock(b);
												ballReacted = true;
											}
											this.blockRemoved();
										}else{
											b.onDraw(canvas);
										}
									}else{
										//if any blocks reach dash line, the game terminates immediately
										gameover();
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
								threshold ++;

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
	
	@Override
	public void initialise(){
		dashLinePaint = getDashLinePaint();
		dashLinePos = initial_y - height/10;
		this.blocks = this.generateBlocks(width, height);
		this.threshold = 0;
		this.paddle = new Paddle(this, null);
		this.ball = new Ball(this, null);
		start();
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
		
		float blockWidth = blocks.get(0).width;
		float blockHeight = blocks.get(0).height;

		
	    
	    for(int i = 0; i < 8; i++){	    
	    		blocks.add(new Block(this, null, padding+blockWidth/2+i*blockWidth, padding+blockHeight/2, blockWidth, blockHeight, i));
	    }
	    
	    this.threshold = 0;
	}	
}

