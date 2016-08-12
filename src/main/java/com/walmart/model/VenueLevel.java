package com.walmart.model;

import java.util.HashMap;
/*
 * This class will maintain the list of the venues in the application
 */
public class VenueLevel {
	private volatile HashMap<Integer,Level> venueLevelList;

	public HashMap<Integer, Level> getVenueLevelList() {
		return venueLevelList;
	}

	public void setVenueLevelList(HashMap<Integer, Level> venueLevelList) {
		this.venueLevelList = venueLevelList;
	}

	

}
