package com.philips.cl.di.dev.pa.datamodel;

import java.util.List;

public class IndoorTrendDto {
	private List<Integer> powerDetailsList ;
	private List<Float> hourlyList ;
	private List<Float> dailyList ;
	private List<Integer> goodAirQualityList;
	private long timeMin;
	
	public long getTimeMin() {
		return timeMin;
	}
	public void setTimeMin(long timeMin) {
		this.timeMin = timeMin;
	}
	
	public List<Integer> getGoodAirQualityList() {
		return goodAirQualityList;
	}
	public void setGoodAirQualityList(List<Integer> goodAirQualityList) {
		this.goodAirQualityList = goodAirQualityList;
	}
	public List<Integer> getPowerDetailsList() {
		return powerDetailsList;
	}
	public void setPowerDetailsList(List<Integer> powerDetailsList) {
		this.powerDetailsList = powerDetailsList;
	}
	public List<Float> getHourlyList() {
		return hourlyList;
	}
	public void setHourlyList(List<Float> hourlyList) {
		this.hourlyList = hourlyList;
	}
	public List<Float> getDailyList() {
		return dailyList;
	}
	public void setDailyList(List<Float> dailyList) {
		this.dailyList = dailyList;
	}
}
