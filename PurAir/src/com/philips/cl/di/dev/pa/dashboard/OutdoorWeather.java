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
		SUNNY(0, R.drawable.sunny),
		CLOUDY(1, R.drawable.cloudy),
		OVERCAST(2, R.drawable.overcast),
		SHOWER(3, R.drawable.shower), //need new
		THUNDERSHOWER(4, R.drawable.thunder_shower), //need new
		THUNDERSHOWER_WITH_HAIL(5, R.drawable.thunder_shower_hail), //need new
		SLEET(6, R.drawable.sleet), //need new
		LIGHT_RAIN(7, R.drawable.light_rain),
		MODERATE_RAIN(8, R.drawable.moderate_rain),
		HEAVY_RAIN(9, R.drawable.heavy_rain),
		STORM(10, R.drawable.storm), //need new
		HEAVY_STORM(11, R.drawable.heavy_storm), //need new
		SEVERE_STORM(12, R.drawable.severe_storm), //need new
		SNOW_FLURRY(13, R.drawable.snow_flurry), //need new
		LIGHT_SNOW(14, R.drawable.light_snow),
		MODERATE_SNOW(15, R.drawable.moderate_snow), //need new
		HEAVY_SNOW(16, R.drawable.heavy_snow), //need new
		SNOWSTORM(17, R.drawable.snow_storm), //need new
		FOGGY(18, R.drawable.foggy), //need new
		ICE_RAIN(19, R.drawable.ice_rain), //need new
		DUSTSTORM(20, R.drawable.duststorm), //need new
		LIGHT_TO_MODERATE_RAIN(21, R.drawable.light_moderate_rain), //need new
		MODERATE_TO_HEAVY_RAIN(22, R.drawable.moderate_heavy_rain), //need new
		HEAVY_RAIN_TO_STORM(23, R.drawable.heavy_rain_storm), //need new
		STORM_TO_HEAVY_STORM(24, R.drawable.storm_heavy_storm), //need new
		HEAVY_TO_SEVERE_STORM(25, R.drawable.heavy_severe_storm), //need new
		LIGHT_TO_MODERATE_SNOW(26, R.drawable.light_moderate_snow), //need new
		MODERATE_TO_HEAVY_SNOW(27, R.drawable.moderate_heavy_snow), //need new
		HEAVY_SNOW_TO_SNOWSTORM(28, R.drawable.heavy_snow_snowstorm), //need new
		DUST(29, R.drawable.dust), //need new
		SAND(30, R.drawable.sand), //need new
		SANDSTORM(31, R.drawable.sandstorm), //need new
		HAZE(53, R.drawable.haze), //need new
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
