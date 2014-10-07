package com.unimelb.breakout.object;

public class Map {

	private String name;
	private int[] map;
	private String nextLevel;
	
	
	public String getNextLevel() {
		return nextLevel;
	}

	public void setNextLevel(String nextLevel) {
		this.nextLevel = nextLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getMap() {
		return map;
	}
	
	public void setMap(int[] map){
		this.map = map;
	}

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