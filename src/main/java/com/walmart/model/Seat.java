package com.walmart.model;

/*
 * This class is the seat object. It will holds the status and position of the seat
 */
public class Seat {
	private volatile boolean isAvailable;
	private volatile boolean isHold;
	private volatile boolean isReserved;
	private volatile int row;
	private volatile int column;
	private volatile int level;
	
	/*
	 * The eblow functions are all the getters and setters for the Seat object parameters
	 */
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	public boolean isHold() {
		return isHold;
	}
	public void setHold(boolean isHold) {
		this.isHold = isHold;
	}
	public boolean isReserved() {
		return isReserved;
	}
	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
