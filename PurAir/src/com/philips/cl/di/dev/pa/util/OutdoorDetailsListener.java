package com.philips.cl.di.dev.pa.util;

import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;

public interface OutdoorDetailsListener {
	public void onOneDayWeatherForecastReceived(List<Weatherdto> weatherList);
	public void onFourDayWeatherForecastReceived(List<ForecastWeatherDto> weatherList);
	public void onAQIHistoricalDataReceived(List<OutdoorAQI> outdoorAQIHistory);
	public void onNearbyLocationsDataReceived(List<OutdoorAQI> nearbyLocationAQIs);
}
