package com.walmart.test;

import java.util.Optional;

import com.walmart.model.SeatHold;
import com.walmart.ticketservice.TicketServiceImpl;
import com.walmart.ticketservice.TicketServiceInitialize;

import junit.framework.TestCase;

/*
 * Junit tests for testing teh reservation functionality
 */
public class TestReserveSeats extends TestCase{
	/*
	 * This function will check whether the application is rejecting the timed out seat hold objects
	 */
	public void testSeatReservationTimeOut(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(3);
		SeatHold hold1 = ticketService.findAndHoldSeats(150, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		try{
            Thread.sleep(15000);
        }
        catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
		String confirmationCode1=ticketService.reserveSeats(hold1.getSeatHoldId(), hold1.getEmailId());
		assertEquals(confirmationCode1,"Reservation is Cancelled due to timeout");
	}
	/*
	 * This function will check whether the seat reservation is success for the valid parameters
	 */
	public void testSeatReservationSuccess(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(3);
		SeatHold hold1 = ticketService.findAndHoldSeats(150, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		String confirmationCode1=ticketService.reserveSeats(hold1.getSeatHoldId(), hold1.getEmailId());
		assertEquals(confirmationCode1,"Reservation is succesful");
	}
	
	
}
