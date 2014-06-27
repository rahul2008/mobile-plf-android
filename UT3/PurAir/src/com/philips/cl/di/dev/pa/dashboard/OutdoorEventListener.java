package com.philips.cl.di.dev.pa.dashboard;

public interface OutdoorEventListener {

//	void outdoorLocationDataReceived(List<City> cities);
	
	void outdoorAQIDataReceived(OutdoorAQI outdoorAQI, String areaID);
	
	void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID);
	
}
