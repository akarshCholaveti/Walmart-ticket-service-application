package com.walmart.model;

import java.util.Arrays;
/*
 * This class will create the layout for each level
 */
public class Level {
	private volatile int price;
	private volatile String levelName;
	private volatile int levelNumber;
	private volatile Seat layout[][];
	private volatile int totalSeatsAvailable;
	private volatile int seatRows[];
	
	public Level(String levelName,int levelNumber,int price,int rows,int columns){
		this.price=price;
		this.levelName=levelName;
		this.levelNumber=levelNumber;
		this.layout=new Seat[rows][columns];
		this.totalSeatsAvailable=rows*columns;
		this.seatRows=new int[rows];
		Arrays.fill(seatRows, columns);
		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				Seat seat=new Seat();
				seat.setAvailable(true);
				seat.setHold(false);
				seat.setReserved(false);
				seat.setRow(i);
				seat.setColumn(j);
				seat.setLevel(levelNumber);
				layout[i][j]=seat;
			}
		}
	}
	
	/*
	 * The below functions are all the regular getters and setters
	 */
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Seat[][] getLayout() {
		return layout;
	}

	public void setLayout(Seat[][] layout) {
		this.layout = layout;
	}

	public int getTotalSeatsAvailable() {
		return totalSeatsAvailable;
	}

	public void setTotalSeatsAvailable(int totalSeatsAvailable) {
		this.totalSeatsAvailable = totalSeatsAvailable;
	}

	public int[] getSeatRows() {
		return seatRows;
	}

	public void setSeatRows(int[] seatRows) {
		this.seatRows = seatRows;
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}
	
}
