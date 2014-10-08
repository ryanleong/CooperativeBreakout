package com.unimelb.breakout.object;

import java.util.ArrayList;

public class ScoreBoard {

	private String name;
	private ArrayList<Rank> ranks;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Rank> getRanks() {
		return ranks;
	}
	public void setRanks(ArrayList<Rank> ranks) {
		this.ranks = ranks;
	}
	
}
