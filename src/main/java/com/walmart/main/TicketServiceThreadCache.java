package com.walmart.main;

import java.util.Optional;

import com.walmart.model.SeatHold;
import com.walmart.ticketservice.TicketServiceImpl;
import com.walmart.ticketservice.TicketServiceInitialize;
/*
 * This thread will run the application with atomic operation for the user
 * The user will complete the operation successfully or will be denied at the entry point.
 * The seat hold clear function will be called periodically by the action listener and it will clean the cache
 */
public class TicketServiceThreadCache implements Runnable{
	
	private String threadName;
	private int minLevel;
	private int maxLevel;
	private int numSeats;
	private String customerEmail;
	
	public TicketServiceThreadCache(String name,int min,int max,int seats,String emailId){
		this.threadName=name;
		this.minLevel = min;
		this.maxLevel = max;
		this.numSeats = seats;
		this.customerEmail = emailId;
	}

	@Override
	public void run() {
		TicketServiceInitialize.threadCount++; //Maintaining the thread count
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		//Getting the availability for the requested levels
		for(int i = minLevel;i<=maxLevel;i++){
			Optional<Integer> level = Optional.ofNullable(i);
			int availability = ticketService.numSeatsAvailable(level);
			System.out.println("The Number of Tickets available for level "+level.get()+" for "+threadName+" is "+availability);
		}
		Optional<Integer> levelMin = Optional.ofNullable(minLevel);
		Optional<Integer> levelmax = Optional.ofNullable(maxLevel);
		SeatHold hold = ticketService.findAndHoldSeats(numSeats, levelMin, levelmax, customerEmail);
		if(hold!=null){//Seathold operation is success
			//Printing the seat hold object details
			System.out.println("The hold Details for "+threadName+
			"The SeatHold Id: "+hold.getSeatHoldId()+
			"Numer of seats are hold: "+hold.getHoldSeats().size()+
			"The total price is: "+hold.getPrice()+
			"The customer Email Id: "+hold.getEmailId());
			TicketServiceInitialize.values.put(hold.getSeatHoldId(), hold);//Adding the object to cache
			//Walmart.com id is being failed to show the reservation will fail if the seathold object is expired
			if(customerEmail.equalsIgnoreCase("walmart.com")){
				try{
		            Thread.sleep(15000); //sleep for 2 minutes 30 seconds
		        }
		        catch (InterruptedException e){
		            Thread.currentThread().interrupt();
		        }
			}
			//Reserving the seats
			String reservationStatus=ticketService.reserveSeats(hold.getSeatHoldId(), hold.getEmailId());
			//Printing the reservation status
			System.out.println("Reservation status for "+threadName+": "+reservationStatus);
		}else{
			System.out.println("The seathold is failed");
		}
		TicketServiceInitialize.threadCount--;
	}

}
