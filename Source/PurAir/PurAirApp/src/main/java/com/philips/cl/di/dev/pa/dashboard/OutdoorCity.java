package com.philips.cl.di.dev.pa.dashboard;

public class OutdoorCity {

	private OutdoorAQI outdoorAQI;
	private OutdoorWeather outdoorWeather;
	private OutdoorCityInfo outdoorCityInfo;

	public OutdoorCityInfo getOutdoorCityInfo() {
		return outdoorCityInfo;
	}

	public void setOutdoorCityInfo(OutdoorCityInfo outdoorCityInfo) {
		this.outdoorCityInfo = outdoorCityInfo;
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
