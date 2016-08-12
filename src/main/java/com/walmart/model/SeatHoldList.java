package com.walmart.model;

import java.util.HashMap;
import java.util.Map;
/*
 * This class will store all the seathold objects created in the application for all the users
 */
public class SeatHoldList {
	private volatile Map<Integer,SeatHold> seatList = new HashMap<Integer,SeatHold>();
	
	public Map<Integer, SeatHold> getSeatList() {
		return seatList;
	}
	public void setSeatList(Map<Integer, SeatHold> seatList) {
		this.seatList = seatList;
	}
	
	
}
