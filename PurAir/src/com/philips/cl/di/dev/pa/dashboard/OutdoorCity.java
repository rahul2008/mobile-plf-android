package com.philips.cl.di.dev.pa.dashboard;

public class OutdoorCity {
	
	private OutdoorAQI outdoorAQI;
	private OutdoorWeather outdoorWeather;
	private String cityName;
	private String cityNameCN;
	
	
	public String getCityNameCN() {
		return cityNameCN;
	}
	public void setCityNameCN(String cityNameCN) {
		this.cityNameCN = cityNameCN;
	}
	
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityName() {
		return cityName;
	}
	
	public void setOutdoorAQI(OutdoorAQI aqi) {
		this.outdoorAQI = aqi;
	}
	public void setOutdoorWeather(OutdoorWeather weather) {
		this.outdoorWeather = weather;
	}
	
	public OutdoorAQI getOutdoorAQI() {
		return outdoorAQI;
	}
	public OutdoorWeather getOutdoorWeather() {
		return outdoorWeather;
	}
	
}
