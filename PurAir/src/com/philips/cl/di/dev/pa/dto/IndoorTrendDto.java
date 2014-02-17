package com.philips.cl.di.dev.pa.dto;

import java.util.List;

public class IndoorTrendDto {
	private List<Integer> powerDetailsList ;
	private List<Float> hourlyList ;
	private List<Float> dailyList ;
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
