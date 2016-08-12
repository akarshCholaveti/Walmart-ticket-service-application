package com.walmart.ticketservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.walmart.model.Level;
import com.walmart.model.Seat;
import com.walmart.model.SeatHold;
import com.walmart.model.VenueLevel;

/*
 * This class will have atomic operation for the user. it will hold all the resources for the user until 
 * the user completes the operation. There will be only one check of user parameters at the beginning
 * of the operation and resource allotment. The synchronized lock will be released after the user completes the operation
 */
public class TicketServiceImpl implements TicketServiceInterface{
	public static TicketServiceImpl ticketService;
	SeatHold hold;
	List<Seat> seatList;
	int price;
	private TicketServiceImpl(){
		
	}
	public static TicketServiceImpl getInstance(){
		if(ticketService == null){
			ticketService = new TicketServiceImpl();
		}
		return ticketService;
	}
	/*
	 * (non-Javadoc)
	 * @see com.walmart.ticketservice.TicketServiceInterface#numSeatsAvailable(java.util.Optional)
	 * This function will give the availability of the given level
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		synchronized(this){
			if(venueLevel.isPresent() && venueLevel.get()>0 && venueLevel.get()<=4)
			{
				return TicketServiceInitialize.venueLevel.getVenueLevelList().get(venueLevel.get()).getTotalSeatsAvailable();
			}
			return 0;
		}
	}
	
	/*
	 * This function will check and clean for the timedout seat hold objects from cache
	 */
	public synchronized void removeHold() {
		TicketServiceInitialize.values.cleanUp();	
	}
	
	/*
	 * This function will invalidate the expired seat hold objects
	 */
	public synchronized void removeSeatHold(SeatHold hold) {
		
			for(Seat seat:hold.getHoldSeats()){
				seat.setAvailable(true);
				seat.setHold(false);
				int level = seat.getLevel();
				int row= seat.getRow();
				Level levelName = TicketServiceInitialize.venueLevel.getVenueLevelList().get(level);
				levelName.setTotalSeatsAvailable(levelName.getTotalSeatsAvailable()+1);
				int seatRows[] = levelName.getSeatRows();
				seatRows[row] = seatRows[row]+1;
				levelName.setSeatRows(seatRows);
			}
		
	}
	/*
	 * (non-Javadoc)
	 * @see com.walmart.ticketservice.TicketServiceInterface#findAndHoldSeats(int, java.util.Optional, java.util.Optional, java.lang.String)
	 * This function will find and hold the seats for the user
	 */
	@Override
	public SeatHold findAndHoldSeats(int numSeats, Optional<Integer> minLevel, Optional<Integer> maxLevel,
			String customerEmail) {
		
			try{
				if(!(minLevel.isPresent() && maxLevel.isPresent() && minLevel.get() >= 1 && maxLevel.get() <=4 && minLevel.get()<=maxLevel.get() 
						&& !customerEmail.isEmpty() && numSeats > 0))
				{
					return null;
				}
				int availableSeats = 0;
				synchronized(VenueLevel.class){
					//checking for the total available seats in the requested level
					for(int i=minLevel.get();i<=maxLevel.get();i++){
						availableSeats += TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getTotalSeatsAvailable();
					}
					if(numSeats<=availableSeats){ //checking for the requested seats is less than available seats
						hold=new SeatHold();
						seatList = new ArrayList<Seat>();
						price = 0;
						for(int i=minLevel.get();i<=maxLevel.get();i++){ //Iterating through each requested level
							if(numSeats>0){ //check if all the requested seats are hold or not
								//Get total seats avaialble in this level
								int seatsAvailableInThisLevel = TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getTotalSeatsAvailable();
								if(seatsAvailableInThisLevel < numSeats){ //check if the available seats are less than requested
									//hold the requested seats in this level
									holdSeats(seatsAvailableInThisLevel,TicketServiceInitialize.venueLevel.getVenueLevelList().get(i),seatList);
									//update the requested seats by subtracting the hold seats
									numSeats -= seatsAvailableInThisLevel;
									//update the price with adding the cost of current booking in this level 
									price += seatsAvailableInThisLevel* TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getPrice();
								}else{
									//hold the requested seats in this level
									holdSeats(numSeats,TicketServiceInitialize.venueLevel.getVenueLevelList().get(i),seatList);
									//update the price with adding the cost of current booking in this level 
									price += numSeats * TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getPrice();
									//update the reuested seats with zero as all the remaining requested seats are held in the above function call
									numSeats = 0;
								}
							}
						}
						//Set the ticket hold details in the seat hold object
						hold.setEmailId(customerEmail);
						hold.setHoldSeats(seatList);
						hold.setPrice(price);
						hold.setReservationTime(new Date());
						hold.setSeatHoldId(TicketServiceInitialize.seatHoldId++);
						return hold;
					}else{
						return null;
					}
					
				}
		}catch(Exception e){
			return null;
		}
	}
	
	/*
	 * This function will hold the requested seats in the given level
	 */
	private void holdSeats(int numSeats, Level level, List<Seat> seatList) {
		int numberOfRows=level.getSeatRows().length; //get the number of rows in the level
		//This iteration will try to hold all the requested seats in a particular row if they are available
		for(int i=0;i<numberOfRows;i++){			//Iterate through each row
			if(numSeats < level.getSeatRows()[i]){  
				
					getSeatsToHold(numSeats,level.getLayout()[i],seatList);
					level.getSeatRows()[i] = level.getSeatRows()[i] - numSeats;
					level.setTotalSeatsAvailable(level.getTotalSeatsAvailable() - numSeats);
				
			}
		}
		// If the holding all the seats in a single row is not possible then hold the seats in the best rows
		for(int i=0;i<numberOfRows;i++){ //Iterating through the rows
			if(numSeats>0){ //check if there are seats to be hold for the user
				
				int seatsAvailableInThisRow = level.getSeatRows()[i]; //get available seats in the row
					//if the seats available are less than the requested hold the entire available seats
					if(seatsAvailableInThisRow < numSeats){ 
						getSeatsToHold(seatsAvailableInThisRow,level.getLayout()[i],seatList);
						level.getSeatRows()[i] = 0; //update the available seats in the row
						numSeats -=seatsAvailableInThisRow;//update the seats to be held
						level.setTotalSeatsAvailable(level.getTotalSeatsAvailable() - seatsAvailableInThisRow);
					}//else book the remaining seats in the row
					else{
						getSeatsToHold(numSeats,level.getLayout()[i],seatList);
						level.getSeatRows()[i] = level.getSeatRows()[i] - numSeats; //update the available seats in the row
						level.setTotalSeatsAvailable(level.getTotalSeatsAvailable() - numSeats);
						numSeats=0; //update the seats to be held
					}
				
			}
		}
	}

	
	/*
	 * hold the requested seats in the given row
	 */
	private void getSeatsToHold(int numSeats, Seat[] seats, List<Seat> seatList) {
		int numberOfSeatsInRow=seats.length;
		for(int i=0;i<numberOfSeatsInRow;i++){ //iterate through each seat in the row
			if(numSeats==0){ //check if all the requested seats are held
				return;
			}
				if(seats[i].isAvailable()){ //check if the seat is available
					seats[i].setAvailable(false); //if available then make held the seat
					seats[i].setHold(true);
					seatList.add(seats[i]);
					numSeats--; //decrement the seat count which are to be held
				}
			
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.walmart.ticketservice.TicketServiceInterface#reserveSeats(int, java.lang.String)
	 * This function is used to reserve the held seats for the user
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		try{
			SeatHold hold= TicketServiceInitialize.values.get(seatHoldId);//Get the seat hold object with the id
			if(hold!=null && hold.getEmailId().equalsIgnoreCase(customerEmail)){ //check for object existence and the email id of the user
				long timeDifference = new Date().getTime() - hold.getReservationTime().getTime(); //calculate the time difference
				if(timeDifference > 12000){ //check if the time difference is greater than 2 minutes
					removeSeatHold(hold);
					return "Reservation is Cancelled due to timeout";
				}
				for(Seat seat:hold.getHoldSeats()){ // reserve the each seat by iterating the seat list
					seat.setHold(false);
					seat.setReserved(true);
				}
				return "Reservation is succesful";
		}
		return "Invalid seatHold Id: Id might be cancelled due to timeout";
		}catch(Exception e){
			return "Invalid seatHold Id: Id might be cancelled due to timeout";
		}
	}

}
