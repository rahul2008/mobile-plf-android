package com.philips.cl.di.dev.pa.dashboard;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto.WindDirection;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto.WindSpeed;

public class OutdoorWeather {

	private int temperature;
	private int humidity;
	private int weatherIcon;
	private String areaID;
	private String time;
	private int windspeedID ;
	private int winddirectionID;
	

	public OutdoorWeather(int temperature, int humidity, int weatherIcon,
			String areaID, String time, int windspeedID, int winddirectionID ) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.weatherIcon = weatherIcon;
		this.areaID = areaID;
		this.time = time;
		this.windspeedID = windspeedID;
		this.winddirectionID = winddirectionID;
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
	
	public int getWindSpeed() {
		return WindSpeed.valueOf(windspeedID);
	}
	
	public int getWindDirection() {
		return WindDirection.valueOf(winddirectionID);
	}
	
	public int getWeatherPhenomena() {
		return PhenomenonInfo.valueOf(weatherIcon);
	}

	
	public enum WindDirection {
		ZERO(0, R.string.no_wind),
		ONE(1, R.string.northeast),
		TWO(2, R.string.east),
		THREE(3, R.string.southeast),
		FOUR(4, R.string.south), 
		FIVE(5, R.string.southwest), 
		SIX(6, R.string.west), 
		SEVEN(7, R.string.northwest), 
		EIGHT(8, R.string.north),
		NINE(9, R.string.whirl_wind);
		
		private int windDirectionID;
		private int windDirectionDes;
		
		private WindDirection(int directionID, int resID) {
			this.windDirectionID = directionID;
			this.windDirectionDes = resID;
		}
		
		public static int valueOf(int windDirectionId) {
			for(WindDirection info : WindDirection.values()) {
				if(info.windDirectionID == windDirectionId) {
					return info.windDirectionDes;
				}
			}
			return ZERO.windDirectionDes;
		}
	}
	
	
	
	public enum PhenomenonInfo {
		SUNNY(0, R.string.sunny), 
		CLOUDY(1, R.string.cloudy), 
		OVERCAST(2,	R.string.overcast), 
		SHOWER(3,R.string.shower),
		THUNDERSHOWER(4, R.string.thundershower), 
		THUNDERSHOWER_WITH_HAIL(5,R.string.thundershower_with_hail),
		SLEET(6,R.string.sleet),
		LIGHT_RAIN(7, R.string.light_rain), 
		MODERATE_RAIN(8,R.string.moderate_rain),
		HEAVY_RAIN(9, R.string.heavy_rain), 
		STORM(10, R.string.storm), 
		HEAVY_STORM(11, R.string.heavy_storm),
		SEVERE_STORM(12, R.string.severe_storm),
		SNOW_FLURRY(13,	R.string.snow_flurry), 
		LIGHT_SNOW(14, R.string.light_snow),
		MODERATE_SNOW(15, R.string.moderate_snow),
		HEAVY_SNOW(16,R.string.heavy_snow),
		SNOWSTORM(17, R.string.snowstorm),
		FOGGY(18, R.string.foggy),
		ICE_RAIN(19, R.string.ice_rain),
		DUSTSTORM(20, R.string.duststorm),
		LIGHT_TO_MODERATE_RAIN(21,R.string.light_to_moderate_rain), 
		MODERATE_TO_HEAVY_RAIN(22,R.string.moderate_to_heavy_rain),
		HEAVY_RAIN_TO_STORM(23,R.string.heavy_rain_to_storm), 
		STORM_TO_HEAVY_STORM(24,R.string.storm_to_heavy_storm),
		HEAVY_TO_SEVERE_STORM(25,R.string.heavy_to_severe_storm),
		LIGHT_TO_MODERATE_SNOW(26,R.string.light_to_moderate_snow), 
		MODERATE_TO_HEAVY_SNOW(27,R.string.moderate_to_heavy_snow), 
		HEAVY_SNOW_TO_SNOWSTORM(28,R.string.heavy_snow_to_snowstorm),
		DUST(29, R.string.dust),
		SAND(30, R.string.sand),
		SANDSTORM(31, R.string.sandstorm),
		HAZE(53, R.string.haze),
		UNKNOWN(99,R.string.unknown);

		private int phenomenonID;
		private int phenomenonDesc;

		private PhenomenonInfo(int phenomenonID, int phenomenonDesc) {
			this.phenomenonID = phenomenonID;
			this.phenomenonDesc = phenomenonDesc;
		}
		public static int valueOf(int weatherId) {
			for (PhenomenonInfo info : PhenomenonInfo.values()) {
				if (info.phenomenonID == weatherId) {
					return info.phenomenonDesc;
				}
			}
			return UNKNOWN.phenomenonDesc;
		}
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

