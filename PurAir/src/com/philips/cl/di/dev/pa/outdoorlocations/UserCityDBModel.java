package com.philips.cl.di.dev.pa.outdoorlocations;

public class UserCityDBModel {
	
	private String areaId;
	private int dataProvider;
	
	public UserCityDBModel(String areaId, int dataProvider) {
		this.areaId = areaId;
		this.dataProvider = dataProvider;
	}

	public String getAreaId() {
		return areaId;
	}

	public int getDataProvider() {
		return dataProvider;
	}
}
