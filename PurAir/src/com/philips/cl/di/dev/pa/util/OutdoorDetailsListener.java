package com.philips.cl.di.dev.pa.util;

import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;

public interface OutdoorDetailsListener {
	void onOneDayWeatherForecastReceived(List<Weatherdto> weatherList);
	void onFourDayWeatherForecastReceived(List<ForecastWeatherDto> weatherList);
	void onAQIHistoricalDataReceived(List<OutdoorAQI> outdoorAQIHistory, String areaId);
	void onNearbyLocationsDataReceived(List<OutdoorAQI> nearbyLocationAQIs);
}
