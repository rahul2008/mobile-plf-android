package com.philips.cl.di.dev.pa.util;

import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.dashboard.OutdoorAQI;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;

public interface OutdoorDetailsListener {
	public void onHourlyWeatherForecastReceived(List<Weatherdto> weatherList) ;
	public void onWeatherForecastReceived(List<ForecastWeatherDto> weatherList) ;
	public void onAQIHisttReceived(List<OutdoorAQI> outdoorAQIHistory) ;
}
