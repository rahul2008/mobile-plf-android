package com.philips.cl.di.dev.pa.dashboard;

import java.util.List;

import com.philips.cl.di.dev.pa.datamodel.Weatherdto;

public interface OutdoorDataListener {

	void allOutdoorAQIDataReceived(List<OutdoorAQI> aqis);
	void outdoorAQIDataReceived(OutdoorAQI outdoorAQI, String areaID);
	void outdoorHistoricalAQIDataReceived(List<OutdoorAQI> aqis);
	//void outdoorWeatherDataReceived(OutdoorWeather outdoorWeather, String areaID);
	void outdoorWeatherDataReceived(List<OutdoorWeather> outdoorWeatherList);
	void outdoorOneDayForecastDataReceived(List<Weatherdto> weatherdtos);
	void outdoorFourDayForecastDataReceived(List<ForecastWeatherDto> weatherDtos);
}
