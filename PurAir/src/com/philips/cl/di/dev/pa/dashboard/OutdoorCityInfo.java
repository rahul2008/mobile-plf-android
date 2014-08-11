package com.philips.cl.di.dev.pa.dashboard;

public class OutdoorCityInfo {

	private String cityName;
	private String cityNameCN;
	private float longitude;
	private float latitude;
	private String areaID;
	private String cityNameTW;
	
	public OutdoorCityInfo(String cityName, String cityNameCN, String cityNameTW, float longitude, float latitude, String areaID) {
		this.areaID = areaID;
		this.cityName = cityName;
		this.cityNameCN = cityNameCN;
		this.cityNameTW= cityNameTW;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public String getCityName() {
		return cityName;
	}

	public String getCityNameCN() {
		return cityNameCN;
	}
	
	public String getCityNameTW() {
		return cityNameTW;
	}

	public float getLongitude() {
		return longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public String getAreaID() {
		return areaID;
	}
	
	
	
}
