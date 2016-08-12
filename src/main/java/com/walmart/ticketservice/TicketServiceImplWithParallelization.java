package com.walmart.ticketservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.walmart.model.Level;
import com.walmart.model.Seat;
import com.walmart.model.SeatHold;
/*
 * This class will have synchronization only at the seat level in order to prevent the 
 * race condition. The user will not have guarantee that all his requested seats will be held for reservation.
 */
public class TicketServiceImplWithParallelization implements TicketServiceInterface {
	
	/*
	 * (non-Javadoc)
	 * @see com.walmart.ticketservice.TicketServiceInterface#numSeatsAvailable(java.util.Optional)
	 * The user will get the seats available for the level at the particular time
	 */
	@Override
	public int numSeatsAvailable(Optional<Integer> venueLevel) {
		if(venueLevel.isPresent() && venueLevel.get()>0 && venueLevel.get()<=4)
		{
			return TicketServiceInitialize.venueLevel.getVenueLevelList().get(venueLevel.get()).getTotalSeatsAvailable();
		}
		return 0;
	}
	
	/*
	 * This function will check and clean the timedout seat hold objects in cache
	 */
	public void removeHold() {
		TicketServiceInitialize.values.cleanUp();
	}
	
	/*
	 * This function will invalidate the expired seat hold objects
	 */
	public void removeSeatHold(SeatHold hold) {
		
			for(Seat seat:hold.getHoldSeats()){
				int level = seat.getLevel();
				int row= seat.getRow();
				synchronized(Seat.class){
					seat.setAvailable(true);
					seat.setHold(false);
					Level levelName = TicketServiceInitialize.venueLevel.getVenueLevelList().get(level);
					levelName.setTotalSeatsAvailable(levelName.getTotalSeatsAvailable()+1);
					int seatRows[] = levelName.getSeatRows();
					seatRows[row] = seatRows[row]+1;
					levelName.setSeatRows(seatRows);
				}
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
			//Check the parameters are valid or not 
			if(!(minLevel.isPresent() && maxLevel.isPresent() && minLevel.get() >= 1 && maxLevel.get() <=4 && minLevel.get()<=maxLevel.get() 
					&& !customerEmail.isEmpty() && numSeats > 0))
			{
				return null;
			}
			int availableSeats = 0;
			//check the cuurent available seats for the level
			for(int i=minLevel.get();i<=maxLevel.get();i++){
				availableSeats += TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getTotalSeatsAvailable();
			}
			//if the available are more than the user requested  then the booking will be proceeded
			if(numSeats < availableSeats){
				SeatHold hold=new SeatHold();
				List<Seat> seatList = new ArrayList<Seat>();
				int price = 0;
				for(int i=minLevel.get();i<=maxLevel.get();i++){
					if(numSeats>0){
						//check the seats available for this level
						int seatsAvailableInThisLevel = TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getTotalSeatsAvailable();
						if(seatsAvailableInThisLevel < numSeats){
							//Call the holdSeats function to hold the seats in the level
							//As there is uncertainty about the complete requested booking, we have the seats held return type
							//which gives the info of seats held in the level
							int seatsHold = holdSeats(seatsAvailableInThisLevel,TicketServiceInitialize.venueLevel.getVenueLevelList().get(i),seatList);
							numSeats -= seatsHold;//updating the number of seats to be held
							price += seatsHold * TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getPrice();//updating the price
						}else{
							//call the holdSeats function to hold the requested seats
							int seatsHold = holdSeats(numSeats,TicketServiceInitialize.venueLevel.getVenueLevelList().get(i),seatList);
							price += seatsHold * TicketServiceInitialize.venueLevel.getVenueLevelList().get(i).getPrice();//update the price
							numSeats = numSeats - seatsHold;//update the number of seats to be held
						}
					}
				}
				//set the values to the seat hold object
				hold.setEmailId(customerEmail);
				hold.setHoldSeats(seatList);
				hold.setPrice(price);
				hold.setReservationTime(new Date());
				hold.setSeatHoldId(TicketServiceInitialize.seatHoldId++);
				return hold;
			}
			return null;
		}catch(Exception e){
			return null;
		}
		
	}
	/*
	 * This function will hold the seats for the user in the given level
	 */
	private int holdSeats(int seatsToHoldInThisLevel, Level level, List<Seat> seatList) {
		int numberOfRows=level.getSeatRows().length;
		int seatsHoldInThisLevel = 0;
		//Iterate through all the seats and hold it if it is available
		for(int i=0;i<numberOfRows;i++){
			int size = level.getLayout()[i].length;
			for(int j=0;j<size;j++){
				if(seatsToHoldInThisLevel>0){
				synchronized(Seat.class){
					if(level.getLayout()[i][j].isAvailable()==true && level.getTotalSeatsAvailable() > 0){ //Hold the seat if available
							Seat seat = level.getLayout()[i][j];
							seat.setAvailable(false);
							seat.setHold(true);
							seatList.add(seat);
							level.setTotalSeatsAvailable(level.getTotalSeatsAvailable() - 1);
							seatsHoldInThisLevel++;
							seatsToHoldInThisLevel--;
						}
					}	
				}
			}
		}
		return seatsHoldInThisLevel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.walmart.ticketservice.TicketServiceInterface#reserveSeats(int, java.lang.String)
	 * This function is used to reserve the held seats for the user
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		try{
			SeatHold hold= TicketServiceInitialize.values.get(seatHoldId); //Get the seat hold object with the id
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
