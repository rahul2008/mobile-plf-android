package com.philips.cl.di.dev.pa.dashboard;

import com.philips.cl.di.dev.pa.R;

public class OutdoorWeather {
	
	private int temperature;
	private int humidity;
	private int weatherIcon;
	private String areaID;
	private String time; 
	
	public OutdoorWeather(int temperature, int humidity, int weatherIcon, String areaID, String time) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.weatherIcon = weatherIcon;
		this.areaID = areaID;
		this.time = time ;
	}
	
	public int getTemperature() {
		return temperature;
	}
	
	public int getHumidity() {
		return humidity;
	}
	
	public int getWeatherIcon() {
		return WeatherInfo.valueOf(weatherIcon);
	}
	
	public String getAreaID() {
		return areaID;
	}
	
	public String getUpdatedTime() {
		return time ;
	}
	
	public enum WeatherInfo {
		SUNNY(0, R.drawable.sunny_white),
		CLOUDY(1, R.drawable.cloudy_white),
		OVERCAST(2, R.drawable.partly_cloudy_white),
		SHOWER(3, R.drawable.light_rain_shower_white), //need new
		THUNDERSHOWER(4, R.drawable.light_rain_shower_white), //need new
		THUNDERSHOWER_WITH_HAIL(5, R.drawable.light_rain_shower_white), //need new
		SLEET(6, R.drawable.light_rain_shower_white), //need new
		LIGHT_RAIN(7, R.drawable.light_rain_shower_white),
		MODERATE_RAIN(8, R.drawable.moderate_rain_with_thunder_white),
		HEAVY_RAIN(9, R.drawable.heavy_rain_white),
		STORM(10, R.drawable.light_rain_shower_white), //need new
		HEAVY_STORM(11, R.drawable.light_rain_shower_white), //need new
		SEVERE_STORM(12, R.drawable.light_rain_shower_white), //need new
		SNOW_FLURRY(13, R.drawable.light_rain_shower_white), //need new
		LIGHT_SNOW(14, R.drawable.snow_white),
		MODERATE_SNOW(15, R.drawable.light_rain_shower_white), //need new
		HEAVY_SNOW(16, R.drawable.light_rain_shower_white), //need new
		SNOWSTORM(17, R.drawable.light_rain_shower_white), //need new
		FOGGY(18, R.drawable.light_rain_shower_white), //need new
		ICE_RAIN(19, R.drawable.light_rain_shower_white), //need new
		DUSTSTORM(20, R.drawable.light_rain_shower_white), //need new
		LIGHT_TO_MODERATE_RAIN(21, R.drawable.light_rain_shower_white), //need new
		MODERATE_TO_HEAVY_RAIN(22, R.drawable.light_rain_shower_white), //need new
		HEAVY_RAIN_TO_STORM(23, R.drawable.light_rain_shower_white), //need new
		STORM_TO_HEAVY_STORM(24, R.drawable.light_rain_shower_white), //need new
		HEAVY_TO_SEVERE_STORM(25, R.drawable.light_rain_shower_white), //need new
		LIGHT_TO_MODERATE_SNOW(26, R.drawable.light_rain_shower_white), //need new
		MODERATE_TO_HEAVY_SNOW(27, R.drawable.light_rain_shower_white), //need new
		HEAVY_SNOW_TO_SNOWSTORM(28, R.drawable.light_rain_shower_white), //need new
		DUST(29, R.drawable.light_rain_shower_white), //need new
		SAND(30, R.drawable.light_rain_shower_white), //need new
		SANDSTORM(31, R.drawable.light_rain_shower_white), //need new
		HAZE(53, R.drawable.light_rain_shower_white), //need new
		UNKNOWN(99, R.drawable.light_rain_shower_white); //need new
		
		private int weatherID;
		private int weatherResID;
		
		private WeatherInfo(int weatherID, int resID) {
			this.weatherID = weatherID;
			this.weatherResID = resID;
		}
		
		public static int valueOf(int weatherId) {
			for(WeatherInfo info : WeatherInfo.values()) {
				if(info.weatherID == weatherId) {
					return info.weatherResID;
				}
			}
			return UNKNOWN.weatherResID;
		}
	}
	
}
