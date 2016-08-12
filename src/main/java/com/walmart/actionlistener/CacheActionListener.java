package com.walmart.actionlistener;

import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.walmart.model.SeatHold;
import com.walmart.ticketservice.TicketServiceImpl;

/*
 * ActionListener for evicted items from the cache after the time out
 */
public class CacheActionListener implements RemovalListener<Integer,SeatHold> {
	
	/*
	 * (non-Javadoc)
	 * @see com.google.common.cache.RemovalListener#onRemoval(com.google.common.cache.RemovalNotification)
	 * The seats from the evicted seathold object are changed to available status in the below function
	 * and there by invalidating the hold status of the object.
	 */
	@Override
	public void onRemoval(RemovalNotification<Integer,SeatHold> record) {
		TicketServiceImpl ticketService=TicketServiceImpl.getInstance();
		ticketService.removeSeatHold(record.getValue());
	}

}
