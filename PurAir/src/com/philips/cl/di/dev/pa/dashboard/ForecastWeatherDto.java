package com.philips.cl.di.dev.pa.dashboard;

import com.philips.cl.di.dev.pa.dashboard.OutdoorWeather.WeatherInfo;

public class ForecastWeatherDto {


	private String fa;
	private String fb;
	private String fc;
	private String fd;
	private String fe;
	private String ff;
	private String fg;
	private String fh;
	private String fi;

	public String getWeatherDay() {
		return fa;
	}

	public String getWeatherNight() {
		return fb;
	}

	public String getTemperatureDay() {
		return fc;
	}

	public String getTemperatureNight() {
		return fd;
	}

	public String getWindDirectionDay() {
		return fe;
	}

	public String getWindDirectionNight() {
		return ff;
	}

	public String getWindSpeedDay() {
		return fg;
	}

	public String getWindSpeedNight() {
		return fh;
	}

	public String getSolarCycleTimes() {
		return fi;
	}
	
	public int getWeatherIcon() {
		int faInt = 0;
		try {
			faInt = Integer.parseInt(fa);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return WeatherInfo.valueOf(faInt);
	}
	
	public int getWindSpeed() {
		int fgInt = 0;
		try {
			fgInt = Integer.parseInt(fg);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return WindSpeed.valueOf(fgInt);
	}
	
	public enum WindSpeed {
		ZERO(0, 8),
		ONE(1, 22),
		TWO(2, 34),
		THREE(3, 47),
		FOUR(4, 62), 
		FIVE(5, 78), 
		SIX(6, 96),
		SEVEN(7, 114), 
		EIGHT(8, 133),
		NINE(9, 152),
		UNKNOWN(10, 0); 
		
		private int windSpeedID;
		private int windSpeedKm;
		
		private WindSpeed(int weatherID, int resID) {
			this.windSpeedID = weatherID;
			this.windSpeedKm = resID;
		}
		
		public static int valueOf(int windSpeedId) {
			for(WindSpeed info : WindSpeed.values()) {
				if(info.windSpeedID == windSpeedId) {
					return info.windSpeedKm;
				}
			}
			return UNKNOWN.windSpeedKm;
		}
	}
	
	public int getWindDirection() {
		int feInt = 0;
		try {
			feInt = Integer.parseInt(fe);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return WindDirection.valueOf(feInt);
	}
	
	public enum WindDirection {
		ZERO(0, 0),
		ONE(1, 45),
		TWO(2, 90),
		THREE(3, 135),
		FOUR(4, 180), 
		FIVE(5, 225), 
		SIX(6, 270), 
		SEVEN(7, 315), 
		EIGHT(8, 0),
		NINE(9, -2),
		UNKNOWN(10, 0); 
		
		private int windDirectionID;
		private int windDirectionAngle;
		
		private WindDirection(int directionID, int resID) {
			this.windDirectionID = directionID;
			this.windDirectionAngle = resID;
		}
		
		public static int valueOf(int windDirectionId) {
			for(WindDirection info : WindDirection.values()) {
				if(info.windDirectionID == windDirectionId) {
					return info.windDirectionAngle;
				}
			}
			return UNKNOWN.windDirectionAngle;
		}
	}

}
