package com.unimelb.breakout.object;

public class UploadResponse {

	private String response;
	private int rank;
	
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
		if(response.equals("Success")){
			return true;
		}
		
		return false;
	}
	
}
