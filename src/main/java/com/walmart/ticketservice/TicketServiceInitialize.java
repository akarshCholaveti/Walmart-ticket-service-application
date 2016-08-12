package com.walmart.ticketservice;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.walmart.actionlistener.CacheActionListener;
import com.walmart.model.Level;
import com.walmart.model.SeatHold;
import com.walmart.model.VenueLevel;

/*
 * This class will create and initialize all the data structures required for the application
 */
public class TicketServiceInitialize {

	public static VenueLevel venueLevel;
	public static Integer seatHoldId = 1;
	//public static SeatHoldList seatHoldList;
	public static LoadingCache<Integer, SeatHold> values;
	public static int threadCount = 0;

	public static void initialize() {
		//Create all the levels
		Level levelOrchestra=new Level("Orchestra",1,100,25,50);
		Level levelMain=new Level("Main",2,75,20,100);
		Level levelBalcony1=new Level("Balcony1",3,50,15,100);
		Level levelBalcony2=new Level("Balcony2",4,40,15,100);
		HashMap<Integer,Level> levelList=new HashMap<Integer,Level>();
		//seatHoldList=new SeatHoldList();
		levelList.put(1,levelOrchestra);
		levelList.put(2,levelMain);
		levelList.put(3,levelBalcony1);
		levelList.put(4,levelBalcony2);
		venueLevel=new VenueLevel();
		venueLevel.setVenueLevelList(levelList);
		
		//Creating the cache
		values = CacheBuilder.newBuilder()
			       .maximumSize(10000)
			       .expireAfterWrite(2L, TimeUnit.MINUTES)
			       .removalListener(new CacheActionListener())
			       .build(
			    		   new CacheLoader<Integer, SeatHold>() {

							@Override
							public SeatHold load(Integer arg0) throws Exception {
								// TODO Auto-generated method stub
								return null;
							}
			    				
			    		           });
		
	}

}
