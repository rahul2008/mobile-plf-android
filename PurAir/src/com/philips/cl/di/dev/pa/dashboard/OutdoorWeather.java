package com.philips.cl.di.dev.pa.dashboard;

import com.philips.cl.di.dev.pa.R;

public class OutdoorWeather {

	private int temperature;
	private int humidity;
	private int weatherIcon;
	private String areaID;
	private String time;

	public OutdoorWeather(int temperature, int humidity, int weatherIcon,
			String areaID, String time) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.weatherIcon = weatherIcon;
		this.areaID = areaID;
		this.time = time;
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
		return time;
	}

	public enum WeatherInfo {
		SUNNY(0, R.drawable.sunny), CLOUDY(1, R.drawable.cloudy), OVERCAST(2,
				R.drawable.overcast), SHOWER(3, R.drawable.shower), THUNDERSHOWER(
				4, R.drawable.thunder_shower), THUNDERSHOWER_WITH_HAIL(5,
				R.drawable.thunder_shower_hail), SLEET(6, R.drawable.sleet), LIGHT_RAIN(
				7, R.drawable.light_rain), MODERATE_RAIN(8,
				R.drawable.moderate_rain), HEAVY_RAIN(9, R.drawable.heavy_rain), STORM(
				10, R.drawable.storm), HEAVY_STORM(11, R.drawable.heavy_storm), SEVERE_STORM(
				12, R.drawable.severe_storm), SNOW_FLURRY(13,
				R.drawable.snow_flurry), LIGHT_SNOW(14, R.drawable.light_snow), MODERATE_SNOW(
				15, R.drawable.moderate_snow), HEAVY_SNOW(16,
				R.drawable.heavy_snow), SNOWSTORM(17, R.drawable.snow_storm), FOGGY(
				18, R.drawable.foggy), ICE_RAIN(19, R.drawable.ice_rain), DUSTSTORM(
				20, R.drawable.duststorm), LIGHT_TO_MODERATE_RAIN(21,
				R.drawable.light_moderate_rain), MODERATE_TO_HEAVY_RAIN(22,
				R.drawable.moderate_heavy_rain), HEAVY_RAIN_TO_STORM(23,
				R.drawable.heavy_rain_storm), STORM_TO_HEAVY_STORM(24,
				R.drawable.storm_heavy_storm), HEAVY_TO_SEVERE_STORM(25,
				R.drawable.heavy_severe_storm), LIGHT_TO_MODERATE_SNOW(26,
				R.drawable.light_moderate_snow), MODERATE_TO_HEAVY_SNOW(27,
				R.drawable.moderate_heavy_snow), HEAVY_SNOW_TO_SNOWSTORM(28,
				R.drawable.heavy_snow_snowstorm), DUST(29, R.drawable.dust), SAND(
				30, R.drawable.sand), SANDSTORM(31, R.drawable.sandstorm), HAZE(
				53, R.drawable.haze), UNKNOWN(99,
				R.drawable.light_rain_shower_white);

		private int weatherID;
		private int weatherResID;

		private WeatherInfo(int weatherID, int resID) {
			this.weatherID = weatherID;
			this.weatherResID = resID;
		}

		public static int valueOf(int weatherId) {
			for (WeatherInfo info : WeatherInfo.values()) {
				if (info.weatherID == weatherId) {
					return info.weatherResID;
				}
			}
			return UNKNOWN.weatherResID;
		}
	}

}
