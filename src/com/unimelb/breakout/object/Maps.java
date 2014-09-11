package com.unimelb.breakout.object;

public class Maps {

	private final static int map_one[] = {1,1,1,1,1,1,1,1,
								   1,1,1,1,1,1,1,1,
								   1,1,1,1,1,1,1,1,
								   1,1,1,1,1,1,1,1,
								   1,1,1,1,1,1,1,1};
	
	private final static int map_two[] = {1,1,1,1,1,1,1,1,
			   					   1,1,1,1,1,1,1,1,
			   					   0,0,0,0,0,0,0,0,
			   					   1,1,1,1,1,1,1,1,
			   					   1,1,1,1,1,1,1,1};
	
	public static int[] getMap(int number){
		switch(number){
		case 1:
			return map_one;
		case 2:
			return map_two;
		}
		return map_one;
	}
}
