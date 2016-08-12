package com.walmart.ticketservice;

import java.util.Optional;

import com.walmart.model.SeatHold;

/*
 * This interface will define the required the functions for the ticket service functionality
 */
public interface TicketServiceInterface {
	
	/*
	 * This function will get the available seats for the requested level
	 */
	int numSeatsAvailable(Optional<Integer> venueLevel);
	/*
	 * This function will find and hold the seats from the requested level
	 */
	SeatHold findAndHoldSeats( int numSeats, Optional<Integer> minLevel,
			Optional<Integer> maxLevel, String customerEmail);
	/*
	 * This function will reserve the seats which are already held by the user
	 */
	String reserveSeats( int seatHoldId, String customerEmail);
}
