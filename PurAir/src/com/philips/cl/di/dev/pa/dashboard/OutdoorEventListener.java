package com.philips.cl.di.dev.pa.dashboard;

import java.util.List;

public interface OutdoorEventListener {

//	void outdoorLocationDataReceived(List<City> cities);
	
	void outdoorAQIDataReceived(OutdoorAQI outdoorAQI, String areaID);
	
	void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID);
	
}
