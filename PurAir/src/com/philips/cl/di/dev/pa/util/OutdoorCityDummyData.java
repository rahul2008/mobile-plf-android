package com.philips.cl.di.dev.pa.util;

import java.util.ArrayList;
import java.util.List;

import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;

public class OutdoorCityDummyData {

	public static float lastDayOutdoorAQIs[] = { 23.0f, 26.0f, 26.0f, 102.0f,
			102.0f, 97.0f, 61.0f, 28.0f, 28.0f, 34.0f, 34.0f, 46.0f, 55.0f,
			43.0f, 43.0f, 35.0f, 35.0f, 38.0f, 38.0f, 46.0f, 46.0f, 35.0f,
			35.0f, 44.0f };
	public static float lastWeekOutdoorAQIs[] = { 23.958334f, 61.75f, 87.125f,
			79.208336f, 87.625f, 77.541664f, 48.809525f };
	public static float lastMonthOutdoorAQIs[] = { 58.5f, 120.333336f,
			184.875f, 314.04166f, 270.16666f, 75.5f, 101.625f, 157.75f,
			263.25f, 350.625f, 400.79166f, 49.125f, 81.083336f, 127.583336f,
			198.875f, 201.45833f, 104.75f, 29.125f, 45.708332f, 107.458336f,
			123.541664f, 23.958334f, 61.75f, 87.125f, 79.208336f, 87.625f,
			77.541664f, 48.809525f };
	
//	public static String fourDayWeatherForecastJson = ""
	public static String fourDayWeatherForecastNightTempratures[] = {"4", "1", "8", "9"};
	public static int fourDayWeatherForecastIcons[] = {0, 3, 32, 53};
	public static int fourDayForecastWindDirection[] = {4, 4, 7, 6};
	public static int fourDayForecastWindSpeed[] = {1, 2, 1, 0};
	
	public static List<ForecastWeatherDto> getFourDayWeatherForecast() {
		List<ForecastWeatherDto> weatherForecasts = new ArrayList<ForecastWeatherDto>();
		
		for (int index = 0; index < 4; index++) {
			ForecastWeatherDto forecastWeatherDto = new ForecastWeatherDto();
//			forecastWeatherDto.set
//			forecastWeatherDto
//			forecastWeatherDto
//			forecastWeatherDto
//			forecastWeatherDto
		}
		
		return weatherForecasts;
	}
}
