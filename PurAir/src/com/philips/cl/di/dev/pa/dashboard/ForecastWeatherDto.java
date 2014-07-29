package com.philips.cl.di.dev.pa.dashboard;

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

}
