package com.unimelb.breakout.utils;

public class Utils {

	/*
	 *Calculate the intersect point of two lines. 
	 *One starts from a1 to a2 and another starts from b1 to b2.
	 */
	public static Point intersect(Point a1, Point a2, Point b1, Point b2){
		
		float x1 = a1.x;
		float x2 = a2.x;
		float x3 = b1.x;
		float x4 = b2.x;
		float y1 = a1.y;
		float y2 = a2.y;
		float y3 = b1.y;
		float y4 = b2.y;
		
		float denom  = ((y4-y3) * (x2-x1)) - ((x4-x3) * (y2-y1));
		  if (denom != 0) {
		    float ua = (((x4-x3) * (y1-y3)) - ((y4-y3) * (x1-x3))) / denom;
		    if ((ua >= 0) && (ua <= 1)) {
		      float ub = (((x2-x1) * (y1-y3)) - ((y2-y1) * (x1-x3))) / denom;
		      if ((ub >= 0) && (ub <= 1)) {
		        float x = x1 + (ua * (x2-x1));
		        float y = y1 + (ua * (y2-y1));
		        return new Point(x,y);
		      }
		    }
		  }
		
		return null;
	}
}
