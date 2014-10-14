package com.unimelb.breakout.utils;

/**
 * Simple Vector Object
 * Used in specific method
 * @author Siyuan Zhang
 *
 */
public class Vector {

	public float x;
	public float y;
	public float magnitude;
	
	public Vector(Point a, Point b){
		x = b.x - a.x;
		y = b.y - a.y;
		magnitude = (float) Math.sqrt(Math.pow(x, 2)
				+Math.pow(y, 2));
	}
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
		magnitude = (float) Math.sqrt(Math.pow(x, 2)
				+Math.pow(y, 2));
	}

	public float dotProduct(Vector v){

		return x * v.x +  y * v.y;
	}
	
	public Vector getUnitVector(){
		return new Vector(x/magnitude, y/magnitude);
	}
}
