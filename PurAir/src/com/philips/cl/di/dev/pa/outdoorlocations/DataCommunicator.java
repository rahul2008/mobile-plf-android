package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.OutdoorDataListener;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public interface DataCommunicator extends ServerResponseListener {
	
	public enum RequestType {
		CITIES_AQI("CITIES_AQI"),
		CITIES_WEATHER("CITIES_WEATHER"),
		HISTORIAL_AQI("HISTORIAL_AQI"),
		FORECAST_ONE_DAY("FORECAST_ONE_DAY"),
		FORECAST_FOUR_DAYS("FORECAST_FOUR_DAYS"),
		ALL_CITIES_AQI("ALL_CITIES_AQI"),
		NEARBY_LOCATIONS_AQI("NEARBY_LOCATIONS_AQI");
		
		private String type;
		
		private RequestType(String type) {
			this.type = type;
		}
		
		public String getRequestTypeString() {
			return this.type;
		}
	}
	
	void requestAQIData(List<String> cities);
	void requestWeatherData(List<String> cities);
	void requestHistoricalAQI(String city);
	void requestOneDayForecast(String city);
	void requestFourDayForecast(String city);
	void setResponseListener(OutdoorDataListener outdoorEventListener);
}
