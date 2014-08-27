package com.philips.cl.di.dev.pa.datamodel;

public class Weatherdto {
	private float tempInCentigrade;
	private float tempInFahrenheit;
	private String date;
	private String time;

	private float windDegree;

	private String windDirection;

	public float getWindDegree() {
		return windDegree;
	}

	public void setWindDegree(float windDegree) {
		this.windDegree = windDegree;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	private float maxTempC;
	private float maxTempF;
	private float minTempC;

	public float getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(float windSpeed) {
		this.windSpeed = windSpeed;
	}

	private float windSpeed;

	public float getMaxTempC() {
		return maxTempC;
	}

	public void setMaxTempC(float maxTempC) {
		this.maxTempC = maxTempC;
	}

	public float getMaxTempF() {
		return maxTempF;
	}

	public void setMaxTempF(float maxTempF) {
		this.maxTempF = maxTempF;
	}

	public float getMinTempC() {
		return minTempC;
	}

	public void setMinTempC(float minTempC) {
		this.minTempC = minTempC;
	}

	public float getMinTempF() {
		return minTempF;
	}

	public void setMinTempF(float minTempF) {
		this.minTempF = minTempF;
	}

	private float minTempF;

	private String weatherDesc;
	private String isdaytime;

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
