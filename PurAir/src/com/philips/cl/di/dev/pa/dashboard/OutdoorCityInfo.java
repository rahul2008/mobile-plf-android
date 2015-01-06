package com.philips.cl.di.dev.pa.dashboard;


public class OutdoorCityInfo implements Comparable<OutdoorCityInfo> {

	private String cityName;
	private String cityNameCN;
	private float longitude;
	private float latitude;
	private String areaID;
	private String cityNameTW;
	private int dataProvider;
	
	public OutdoorCityInfo(String cityName, String cityNameCN, String cityNameTW, float longitude, float latitude, String areaID, int dataProvider) {
		this.areaID = areaID;
		this.cityName = cityName;
		this.cityNameCN = cityNameCN;
		this.cityNameTW= cityNameTW;
		this.longitude = longitude;
		this.latitude = latitude;
		this.dataProvider = dataProvider;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public void setCityNameCN(String cityNameCN) {
		this.cityNameCN = cityNameCN;
	}
	
	public void setCityNameTW(String cityNameTW) {
		this.cityNameTW = cityNameTW;
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
	
	public int getDataProvider() {
		return dataProvider;
	}

	@Override
	public int compareTo(OutdoorCityInfo another) {
		// TODO Auto-generated method stub
		return this.cityName.compareTo(another.cityName);
	}
}
