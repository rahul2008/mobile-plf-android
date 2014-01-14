package com.philips.cl.di.dev.pa.dto;

public class Weatherdto {
	private float tempInCentigrade ;
	private float tempInFahrenheit ;
	private String date ;
	private String time ;
	
	private String weatherDesc ;
	private String isdaytime ;

	public float getTempInCentigrade() {
		return tempInCentigrade;
	}

	public void setTempInCentigrade(float tempInCentigrade) {
		this.tempInCentigrade = tempInCentigrade;
	}

	public float getTempInFahrenheit() {
		return tempInFahrenheit;
	}

	public void setTempInFahrenheit(float tempInFahrenheit) {
		this.tempInFahrenheit = tempInFahrenheit;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getWeatherDesc() {
		return weatherDesc;
	}

	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}
	
	public String getIsdaytime() {
		return isdaytime;
	}

	public void setIsdaytime(String isdaytime) {
		this.isdaytime = isdaytime;
	}
	
}
