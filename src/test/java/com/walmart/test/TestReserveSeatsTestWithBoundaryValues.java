package com.walmart.test;

import java.util.Optional;

import com.walmart.model.SeatHold;
import com.walmart.ticketservice.TicketServiceImpl;
import com.walmart.ticketservice.TicketServiceInitialize;

import junit.framework.TestCase;
/*
 * Junit tests to check the reservation functionality for the boundary values
 */
public class TestReserveSeatsTestWithBoundaryValues extends TestCase {
	
	/*
	 * Test seat reservation with no seat hold Id
	 */
	public void testReserveSeatsWithNoIdCreated(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		String confirmationCode = ticketService.reserveSeats(0, "akarsh.cholaveti@gmail.com");
		assertEquals(confirmationCode,"Invalid seatHold Id: Id might be cancelled due to timeout");
	}
	
	/*
	 * Test seat reservation with the wrong email id 
	 */
	public void testReserveSeatsWithWrongEmailId(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(3);
		SeatHold hold1 = ticketService.findAndHoldSeats(150, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		String confirmationCode1=ticketService.reserveSeats(hold1.getSeatHoldId(), "akarsh1.cholaveti@gmail.com");
		assertEquals(confirmationCode1,"Invalid seatHold Id: Id might be cancelled due to timeout");
	}

}
