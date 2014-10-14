package com.unimelb.breakout.object;

/**
 * JSON Object
 * 
 * @author Siyuan Zhang
 *
 */
public class Map {

	private String name;
	private int[][] map;
	private float blockWidth;
	private float blockHeight;
	private int row;
	private int column;
	private int initialX;
	private int initialY;
	private int paddleWidth;
	private int paddleHeight;
	private int ballInitialXSpeed;
	private int ballInitialYSpeed;


	public int getBallInitialXSpeed() {
		return ballInitialXSpeed;
	}

	public void setBallInitalXSpeed(int ballInitalXSpeed) {
		this.ballInitialXSpeed = ballInitalXSpeed;
	}

	public int getBallInitialYSpeed() {
		return ballInitialYSpeed;
	}

	public void setBallInitialYSpeed(int ballInitialYSpeed) {
		this.ballInitialYSpeed = ballInitialYSpeed;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getInitialX() {
		return initialX;
	}

	public void setInitialX(int initialX) {
		this.initialX = initialX;
	}

	public int getInitialY() {
		return initialY;
	}

	public void setInitialY(int initialY) {
		this.initialY = initialY;
	}

	public int getPaddleWidth() {
		return paddleWidth;
	}

	public void setPaddleWidth(int paddleWidth) {
		this.paddleWidth = paddleWidth;
	}

	public int getPaddleHeight() {
		return paddleHeight;
	}

	public void setPaddleHeight(int paddleHeight) {
		this.paddleHeight = paddleHeight;
	}

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
	
}
