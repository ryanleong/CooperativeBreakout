package com.unimelb.breakout.utils;

/**
 * Simple point object.
 * Used in specific method
 * 
 * @author Siyuan Zhang
 *
 */
public class Point {

	public float x; 
	public float y;
	
	public Point(float x, float y){
		this.x = x;
		this.y = y;
	}

	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
	}
}
