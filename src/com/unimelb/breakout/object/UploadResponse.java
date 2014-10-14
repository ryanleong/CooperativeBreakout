package com.unimelb.breakout.object;

/**
 * JSON Object
 * @author Siyuan Zhang
 *
 */
public class UploadResponse {

	private String response;
	private String name;
	private int score;
	private int rank;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public boolean isSuccess(){
		if(response.equals("success")){
			return true;
		}
		
		return false;
	}
	
}
