package com.philips.cl.di.dev.pa.dto;

public class OutdoorAQIEventDto {
	private int outdoorAQI ;
	private String syncDateTime ;
	private int cityID ;
	
	public int getOutdoorAQI() {
		return outdoorAQI;
	}
	public void setOutdoorAQI(int outdoorAQI) {
		this.outdoorAQI = outdoorAQI;
	}
	public String getSyncDateTime() {
		return syncDateTime;
	}
	public void setSyncDateTime(String syncDateTime) {
		this.syncDateTime = syncDateTime;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
}
