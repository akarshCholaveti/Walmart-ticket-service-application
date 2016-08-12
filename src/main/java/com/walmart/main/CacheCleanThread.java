package com.walmart.main;

import com.walmart.ticketservice.TicketServiceInitialize;

/*
 * This thread will clean the cache for every 2 minutes
 * 
 * To ease the testing, the cache cleaning call iterations are limited to 20 times
 * 
 */
public class CacheCleanThread implements Runnable {

	@Override
	public void run() {
		while(TicketServiceInitialize.threadCount > 0){ // Checks for active threads in the application
			TicketServiceInitialize.values.cleanUp(); //cache clean up function
			try{
	            Thread.sleep(12000);
	        }
	        catch (InterruptedException e){
	            Thread.currentThread().interrupt();
	        }
		}
	}

}
