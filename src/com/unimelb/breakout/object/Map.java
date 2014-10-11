package com.unimelb.breakout.object;

public class Map {

	private String name;
	private int[][] map;
	private float blockWidth;
	private float blockHeight;

	public String getName() {
		return name;
	}

	public float getBlockWidth() {
		return blockWidth;
	}

	public void setBlockWidth(float blockWidth) {
		this.blockWidth = blockWidth;
	}

	public float getBlockHeight() {
		return blockHeight;
	}

	public void setBlockHeight(float blockHeight) {
		this.blockHeight = blockHeight;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[][] getMap() {
		return map;
	}
	
	public void setMap(int[][] map){
		this.map = map;
	}

//	private final static int map_one[] = {1,1,1,1,1,1,1,1,
//								   1,1,1,1,1,1,1,1,
//								   1,1,1,1,1,1,1,1,
//								   1,1,1,1,1,1,1,1,
//								   1,1,1,1,1,1,1,1};
//	
//	private final static int map_two[] = {1,1,1,1,1,1,1,1,
//			   					   1,1,1,1,1,1,1,1,
//			   					   0,0,0,0,0,0,0,0,
//			   					   1,1,1,1,1,1,1,1,
//			   					   1,1,1,1,1,1,1,1};
//	
//	public static int[] getMap(int number){
//		switch(number){
//		case 1:
//			return map_one;
//		case 2:
//			return map_two;
//		}
//		return map_one;
//	}
	
}
