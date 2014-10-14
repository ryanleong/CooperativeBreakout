package com.unimelb.breakout.object;

import java.util.ArrayList;

/**
 * JSON Object
 * @author Siyuan Zhang
 *
 */
public class ScoreBoard {

	private String name;
	private ArrayList<Rank> scores;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Rank> getScores() {
		return scores;
	}
	public void setRanks(ArrayList<Rank> scores) {
		this.scores = scores;
	}
	
}
