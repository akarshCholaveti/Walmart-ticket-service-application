package com.walmart.model;

import java.util.Date;
import java.util.List;

/*
 * The seathold class will save the details of the user seats for temporary holding
 */
public class SeatHold {
	private volatile List<Seat> holdSeats;
	private volatile int price;
	private volatile String emailId;
	private volatile int seatHoldId;
	private volatile Date reservationTime;
	
	/*
	 * The below functions are the getters and setters for the SeatHold class 
	 */
	public List<Seat> getHoldSeats() {
		return holdSeats;
	}
	public void setHoldSeats(List<Seat> holdSeats) {
		this.holdSeats = holdSeats;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public int getSeatHoldId() {
		return seatHoldId;
	}
	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}
	public Date getReservationTime() {
		return reservationTime;
	}
	public void setReservationTime(Date reservationTime) {
		this.reservationTime = reservationTime;
	}
	
	
}
