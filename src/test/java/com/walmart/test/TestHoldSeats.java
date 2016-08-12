package com.walmart.test;

import java.util.Optional;

import com.walmart.model.SeatHold;
import com.walmart.ticketservice.TicketServiceImpl;
import com.walmart.ticketservice.TicketServiceInitialize;

import junit.framework.TestCase;
/*
 * Junit tests for testing the hold seats functionality
 */
public class TestHoldSeats extends TestCase{
	
	/*
	 * Test to hold all the seats of the given level
	 */
	public void testHoldAllSeats(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(4);
		SeatHold hold1 = ticketService.findAndHoldSeats(6250, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		int availableSeats = 0;
		for(int i=1;i<=4;i++){
			Optional<Integer> venueLevel = Optional.ofNullable(i);
			availableSeats+=ticketService.numSeatsAvailable(venueLevel);
		}
		assertEquals(availableSeats,0);
	}
	
	/*
	 * Test hold seats consecutively of the given level
	 */
	public void testHoldSeatsConsecutively(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(1);
		SeatHold hold1 = ticketService.findAndHoldSeats(650, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		try{
            Thread.sleep(15000);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
		ticketService.removeHold();
		hold1 = ticketService.findAndHoldSeats(650, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		int availableSeats = ticketService.numSeatsAvailable(minLevel);
		assertEquals(availableSeats,600);
	}
	
	/*
	 * Test hold seats consecutively with no delay of the given level
	 */
	public void testHoldSeatsConsecutivelyWithNoDelay(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(1);
		SeatHold hold1 = ticketService.findAndHoldSeats(600, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		SeatHold hold2 = ticketService.findAndHoldSeats(600, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold2.getSeatHoldId(),hold2);
		int availableSeats = ticketService.numSeatsAvailable(minLevel);
		assertEquals(availableSeats,50);
	}
	
	/*
	 * Test hold seats with requesting seats more than capacity
	 */
	public void testHoldSeatsThanCapacity(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(4);
		SeatHold hold1 = ticketService.findAndHoldSeats(6500, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		int availableSeats = 0;
		for(int i=1;i<=4;i++){
			Optional<Integer> venueLevel = Optional.ofNullable(i);
			availableSeats+=ticketService.numSeatsAvailable(venueLevel);
		}
		assertEquals(availableSeats,6250);
		assertEquals(hold1,null);
	}
	
	/*
	 * Test the hold seats with non positive values
	 */
	public void testForNonPositiveValues(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(1);
		SeatHold hold1 = ticketService.findAndHoldSeats(-10, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		SeatHold hold2 = ticketService.findAndHoldSeats(0, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		assertEquals(hold1,null);
		assertEquals(hold2,null);
	}
	/*
	 * Test the hold seats with the correct values
	 */
	public void testForCorrectSeatHoldValues(){
		TicketServiceInitialize.initialize();
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		Optional<Integer> minLevel = Optional.ofNullable(1);
        Optional<Integer> maxLevel = Optional.ofNullable(1);
        int seatAvailabilityBeforeHolding = ticketService.numSeatsAvailable(minLevel);
		SeatHold hold1 = ticketService.findAndHoldSeats(100, minLevel, maxLevel, "akarsh.cholaveti@gmail.com");
		TicketServiceInitialize.values.put(hold1.getSeatHoldId(), hold1);
		int seatAvailabilityAfterHolding = ticketService.numSeatsAvailable(minLevel);
		assertEquals(seatAvailabilityBeforeHolding,seatAvailabilityAfterHolding+100);
		assertEquals(hold1.getHoldSeats().size(),100);
	}
}
