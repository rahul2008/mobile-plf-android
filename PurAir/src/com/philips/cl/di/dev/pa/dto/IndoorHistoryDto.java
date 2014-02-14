package com.philips.cl.di.dev.pa.dto;

public class IndoorHistoryDto {
	private String timeStamp ;
	private float aqi ;
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public float getAqi() {
		return aqi;
	}
	public void setAqi(float aqi) {
		this.aqi = aqi;
	}
	public long getTfav() {
		return tfav;
	}
	public void setTfav(long tfav) {
		this.tfav = tfav;
	}
	private long tfav ;
}
