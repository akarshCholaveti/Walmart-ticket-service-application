package com.walmart.test;

import java.util.Optional;

import com.walmart.ticketservice.TicketServiceImpl;
import com.walmart.ticketservice.TicketServiceInitialize;

import junit.framework.TestCase;
/*
 * Junit tests for checking the availability of seats
 */
public class TestAvailableSeats extends TestCase{
	
	/*
	 * Test availability value for the given level
	 */
	public void testAvailabilityForTheGivenLevel(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> level = Optional.ofNullable(1);
		int availability = ticketService.numSeatsAvailable(level);
		assertEquals(availability,1250);
	}
	
	/*
	 * Test availability for the null level value
	 */
	public void testAvailabilityForTheNullValue(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> level = Optional.ofNullable(null);
		int availability = ticketService.numSeatsAvailable(level);
		assertEquals(availability,0);
	}
	
	/*
	 * Test availability for the negative value
	 */
	public void testAvailabilityForTheNegativeValue(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> level = Optional.ofNullable(-1);
		int availability = ticketService.numSeatsAvailable(level);
		assertEquals(availability,0);
	}
	/*
	 * Test availability for the out of bound value
	 */
	public void testAvailabilityForTheOutOfBoundLevelValue(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> level = Optional.ofNullable(5);
		int availability = ticketService.numSeatsAvailable(level);
		assertEquals(availability,0);
	}
}
