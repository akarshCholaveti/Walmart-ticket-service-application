package com.walmart.test;

import java.util.Optional;

import com.walmart.model.SeatHold;
import com.walmart.ticketservice.TicketServiceImpl;
import com.walmart.ticketservice.TicketServiceInitialize;

import junit.framework.TestCase;
/*
 * Junit tests for the out of bound values for the HoldSeats function
 */
public class TestHoldSeatsForNullValues extends TestCase {
	
	/*
	 * Test Hold zero seats
	 */
	public void testHoldZeroSeats(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.of(1);
        Optional<Integer> maxLevel = Optional.of(2);
		SeatHold hold=ticketService.findAndHoldSeats(0, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		assertTrue(hold==null);
	}
	/*
	 * Test Hold seats with null values
	 */
	
	public void testHoldSeatsWithNullLevels(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		SeatHold hold=ticketService.findAndHoldSeats(20, null, null, "akarsh.cholaveti@gmail.com");
		assertTrue(hold==null);
	}
	/*
	 * Test hold seats with boundary values
	 */
	public void testHoldSeatsWithOutofBoundLevelValues(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.of(6);
        Optional<Integer> maxLevel = Optional.of(9);
		SeatHold hold=ticketService.findAndHoldSeats(20, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		assertTrue(hold==null);
	}
	/*
	 * Test hold seats with empty email id
	 */
	public void testHoldSeatsWithEmptyEmailId(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.of(1);
        Optional<Integer> maxLevel = Optional.of(2);
		SeatHold hold=ticketService.findAndHoldSeats(20, minLevel, maxLevel, "");
		assertTrue(hold==null);
	}
}
