package com.philips.cl.di.dev.pa.outdoorlocations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.dashboard.ForecastWeatherDto;
import com.philips.cl.di.dev.pa.datamodel.Weatherdto;
import com.philips.cl.di.dev.pa.util.DashboardUtil;
import com.philips.cl.di.dev.pa.util.DataParser;

/**
 * Dummy data for outdoor location, if App in demo mode, there is no Internet. 
 * So have to show dummy data for outdoor locations.
 * */
public class DummyData {

	private static DummyData instance;
	private HashMap<String, Integer> outdoorAqiMap;
	private HashMap<String, Integer> outdoorPmMap;
	private List<Weatherdto> hourlyForecasts;
	public static float lastDayOutdoorAQIsShanghai[] = { 102.0f, 126.0f, 126.0f, 102.0f,
		102.0f, 197.0f, 161.0f, 128.0f, 128.0f, 134.0f, 134.0f, 146.0f, 255.0f,
		243.0f, 343.0f, 235.0f, 435.0f, 438.0f, 438.0f, 446.0f, 446.0f, 435.0f,
		435.0f, 475.0f };
	public static float lastWeekOutdoorAQIsShanghai[] = { 423.958334f, 461.75f,
		487.125f, 479.208336f, 487.625f, 477.541664f, 448.809525f };
	public static float lastMonthOutdoorAQIsShanghai[] = { 258.5f, 120.333336f,
		184.875f, 314.04166f, 270.16666f, 275.5f, 101.625f, 157.75f,
		263.25f, 350.625f, 400.79166f, 49.125f, 81.083336f, 127.583336f,
		198.875f, 201.45833f, 104.75f, 329.125f,345.708332f, 107.458336f,
		123.541664f, 423.958334f, 461.75f, 487.125f, 479.208336f, 487.625f,
		477.541664f, 448.809525f };
	
	public static float lastDayIndoorAQIs[] = { 1f, 1f, 1f, 1.6f, 2f, 1f, .8f, .3f,
			1f, 1f, 1f, -1f, .7f, .5f, .5f, .4f, -1f, 1f, 1f, 2f, 1.2f, 2f, 1f, 1f };
	public static float lastWeekIndoorAQIs[] = { .2f, 1.2f,	1f, 1f, .5f, .6f, .4f };
	public static float lastMonthIndoorAQIs[] = { 1f, .4f, .5f, .4f, .3f, .2f,
			.2f, 1f, 1f, 1f, .2f, .3f, .8f, .3f, .3f, 1f, 1f, 1f, .6f, .6f,
			1.1f, .2f, 1.2f, 1f, 1f, .5f, .6f, .4f };

	public static float lastDayOutdoorAQIsBeijing[] = { 202.0f, 226.0f, 226.0f, 202.0f,
		102.0f, 197.0f, 161.0f, 128.0f, 128.0f, 234.0f, 234.0f, 146.0f, 255.0f,
		243.0f, 243.0f, 235.0f, 435.0f, 438.0f, 438.0f, 346.0f, 346.0f, 335.0f,
		335.0f, 375.0f };
	public static float lastWeekOutdoorAQIsBeijing[] = { 223.958334f, 361.75f,
		387.125f, 379.208336f, 487.625f, 433.541664f, 348.809525f };
	public static float lastMonthOutdoorAQIsBeijing[] = { 258.5f, 120.333336f,
		184.875f, 314.04166f, 270.16666f, 275.5f, 101.625f, 157.75f,
		263.25f, 350.625f, 400.79166f, 49.125f, 81.083336f, 127.583336f,
		198.875f, 201.45833f, 104.75f, 329.125f,345.708332f, 107.458336f,
		123.541664f, 223.958334f, 361.75f, 387.125f, 379.208336f, 487.625f,
		433.541664f, 348.809525f };

	public static float lastDayOutdoorAQIs[] = { 202.0f, 226.0f, 226.0f, 202.0f,
		102.0f, 197.0f, 161.0f, 128.0f, 128.0f, 234.0f, 234.0f, 146.0f, 255.0f,
		143.0f, 143.0f, 135.0f, 335.0f, 338.0f, 338.0f, 346.0f, 346.0f, 235.0f,
		235.0f, 275.0f };
	public static float lastWeekOutdoorAQIs[] = { 123.958334f, 261.75f,
		287.125f, 279.208336f, 387.625f, 333.541664f, 348.809525f };
	public static float lastMonthOutdoorAQIs[] = { 158.5f, 220.333336f,
		284.875f, 214.04166f, 270.16666f, 275.5f, 101.625f, 157.75f,
		263.25f, 250.625f, 200.79166f, 49.125f, 81.083336f, 127.583336f,
		198.875f, 201.45833f, 104.75f, 229.125f,245.708332f, 107.458336f,
		123.541664f, 123.958334f, 261.75f, 287.125f, 279.208336f, 387.625f,
		333.541664f, 348.809525f };

	public static String hourlyTimeStamps[] = { "2014-11-13 12:00:00.0",
		"2014-11-13 13:00:00.0", "2014-11-13 14:00:00.0",
		"2014-11-13 15:00:00.0", "2014-11-13 16:00:00.0",
		"2014-11-13 17:00:00.0", "2014-11-13 18:00:00.0",
		"2014-11-13 19:00:00.0", "2014-11-13 20:00:00.0",
		"2014-11-13 21:00:00.0", "2014-11-13 22:00:00.0",
		"2014-11-13 23:00:00.0", "2014-11-13 00:00:00.0",
		"2014-11-13 1:00:00.0", "2014-11-13 2:00:00.0",
		"2014-11-13 3:00:00.0", "2014-11-13 4:00:00.0",
		"2014-11-13 5:00:00.0", "2014-11-13 6:00:00.0",
		"2014-11-13 7:00:00.0", "2014-11-13 8:00:00.0",
		"2014-11-13 9:00:00.0", "2014-11-13 10:00:00.0",
	"2014-11-13 11:00:00.0" };

	public static float hourlyTempratures[] = { 5f, 5f, 5f, 5f, 5f, 5f, 5f, 4f,
		4f, 4f, 4f, 2f, 3f, 5f, 5f, 5f, 10f, 10f, 10f, 5f, 5f, 5f, 5f, 5f };

	public static String hourlyDescriptions[] = { "mostlyclear", "mostlyclear",
		"mostlyclear", "mostlyclear", "mostlyclear", "mostlyclear",
		"mostlyclear", "mostlyclear", "mostlyclear", "cloudy", "cloudy",
		"cloudy", "cloudy", "cloudy", "mostlyclear", "mostlyclear",
		"mostlyclear", "mostlyclear", "mostlyclear", "mostlyclear",
		"mostlyclear", "mostlyclear", "mostlyclear", "mostlyclear" };

	public static String fourDayWeatherForecastJson = "{\"forecast4d\":{\"101270101\":"
			+ "{\"c\":{\"c1\":\"101270101\",\"c2\":\"chengdu\",\"c3\":\"\",\"c4\":\"chengdu\",\"c5\":\"\",\"c6\":\"sichuan\",\"c7\":\"\",\"c8\":\"china\",\"c9\":\"\",\"c10\":\"1\",\"c11\":\"028\",\"c12\":\"610000\",\"c13\":\"104.066541\",\"c14\":\"30.572269\",\"c15\":\"507\",\"c16\":\"AZ9280\",\"c17\":\"+8\"},"
			+ "\"f\":{\"f1\":["
			+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"33\",\"fd\":\"24\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:18|20:01\"},"
			+ "{\"fa\":\"01\",\"fb\":\"03\",\"fc\":\"32\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:19|20:00\"},"
			+ "{\"fa\":\"02\",\"fb\":\"08\",\"fc\":\"31\",\"fd\":\"23\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|20:00\"},"
			+ "{\"fa\":\"03\",\"fb\":\"08\",\"fc\":\"30\",\"fd\":\"22\",\"fe\":\"4\",\"ff\":\"4\",\"fg\":\"0\",\"fh\":\"0\",\"fi\":\"06:20|19:59\"}],"
			+ "\"f0\":\"201407281100\"}}}}";

	private DummyData() {
		outdoorAqiMap = new HashMap<String, Integer>();
		outdoorPmMap = new HashMap<String, Integer>();
		hourlyForecasts = new ArrayList<Weatherdto>();
	}

	public int getAqi(String areaId) {
		int aqi = 450;
		if (outdoorAqiMap.containsKey(areaId)) {
			aqi = outdoorAqiMap.get(areaId);
		} else {
			aqi = getRundomNum(areaId);
			outdoorAqiMap.put(areaId, aqi);
		}
		return aqi;
	}

	public int getPmTwoPointFive(int aqi, String areaId) {
		int pm2point5 = 115;
		if (outdoorPmMap.containsKey(areaId)) {
			pm2point5 = outdoorPmMap.get(areaId);
		} else {
			pm2point5 = getRundomPm2Point5(aqi);
			outdoorPmMap.put(areaId, pm2point5);
		}
		return pm2point5;
	}

	public float[] getLastDayAqis(String areaId) {
		float[] lastDayAqis = lastDayOutdoorAQIs;
		if (areaId.equals(AppConstants.SHANGHAI_AREA_ID)) {
			lastDayAqis = lastDayOutdoorAQIsShanghai;
		} else if (areaId.equals(AppConstants.BEIJING_AREA_ID)) {
			lastDayAqis = lastDayOutdoorAQIsBeijing;
		} 
		return lastDayAqis;
	}

	public float[] getLastWeekAqis(String areaId) {
		float[] lastWeekAqis = lastWeekOutdoorAQIs;
		if (areaId.equals(AppConstants.SHANGHAI_AREA_ID)) {
			lastWeekAqis = lastWeekOutdoorAQIsShanghai;
		} else if (areaId.equals(AppConstants.BEIJING_AREA_ID)) {
			lastWeekAqis = lastWeekOutdoorAQIsBeijing;
		} 
		return lastWeekAqis;
	}

	public float[] getLastMonthAqis(String areaId) {
		float[] lastMonthAqis = lastMonthOutdoorAQIs;
		if (areaId.equals(AppConstants.SHANGHAI_AREA_ID)) {
			lastMonthAqis = lastMonthOutdoorAQIsShanghai;
		} else if (areaId.equals(AppConstants.BEIJING_AREA_ID)) {
			lastMonthAqis = lastMonthOutdoorAQIsBeijing;
		} 
		return lastMonthAqis;
	}

	private int getRundomPm2Point5(int aqi) {
		if (aqi >= 200 && aqi < 300) {
			return 145;
		} else if (aqi >= 300) {
			return 165;
		} else {
			return 115;
		}
	}

	private int getRundomNum(String areaId) {
		if (areaId.equals(AppConstants.SHANGHAI_AREA_ID) 
				||  areaId.equals(AppConstants.BEIJING_AREA_ID)) {
			return DashboardUtil.getRundomNumber(400, 500);
		} else {
			return DashboardUtil.getRundomNumber(150, 500);
		}
	}

	public List<ForecastWeatherDto> getFourDayWeatherForecast() {
		return DataParser.parseFourDaysForecastData(fourDayWeatherForecastJson);
	}

	public List<Weatherdto> getTodayWeatherForecast() {
		if (hourlyForecasts.isEmpty()) {
			for (int index = 0; index < 24; index++) {
				Weatherdto weatherdto = new Weatherdto();
				weatherdto.setTempInCentigrade(hourlyTempratures[index]);
				weatherdto.setTime(hourlyTimeStamps[index]);
				weatherdto.setWeatherDesc(hourlyDescriptions[index]);
				hourlyForecasts.add(weatherdto);
			}
		}
		return hourlyForecasts; 
	}

	public synchronized static DummyData getInstance() {
		if (instance == null) {
			instance = new DummyData();
		}
		return instance;
	}
}
