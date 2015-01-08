package com.philips.cl.di.dev.pa.dashboard;

import com.philips.cl.di.dev.pa.R;

public enum OutdoorImage {

	AQI_NONE_SHANGHAI(100, R.drawable.air_dashboard_outdoor_shanghai_50), AQI_FIFTY_SHANGHAI(
			101, R.drawable.air_dashboard_outdoor_shanghai_50), AQI_HUNDRED_SHANGHAI(
			102, R.drawable.air_dashboard_outdoor_shanghai_100), AQI_ONE_FIFTY_SHANGHAI(
			103, R.drawable.air_dashboard_outdoor_shanghai_150), AQI_TWO_HUNDRED_SHANGHAI(
			104, R.drawable.air_dashboard_outdoor_shanghai_200), AQI_THREE_HUNDRED_SHANGHAI(
			105, R.drawable.air_dashboard_outdoor_shanghai_300), AQI_FIVE_HUNDRED_SHANGHAI(
			106, R.drawable.air_dashboard_outdoor_shanghai_500),

	AQI_NONE_BEIJING(200, R.drawable.air_dashboard_outdoor_beijing_50), AQI_FIFTY_BEIJING(
			201, R.drawable.air_dashboard_outdoor_beijing_50), AQI_HUNDRED_BEIJING(
			202, R.drawable.air_dashboard_outdoor_beijing_100), AQI_ONE_FIFTY_BEIJING(
			203, R.drawable.air_dashboard_outdoor_beijing_150), AQI_TWO_HUNDRED_BEIJING(
			204, R.drawable.air_dashboard_outdoor_beijing_200), AQI_THREE_HUNDRED_BEIJING(
			205, R.drawable.air_dashboard_outdoor_beijing_300), AQI_FIVE_HUNDRED_BEIJING(
			206, R.drawable.air_dashboard_outdoor_beijing_500),

	AQI_NONE_CHONHGING(300, R.drawable.air_dashboard_outdoor_chongqing_50), AQI_FIFTY_CHONHGING(
			301, R.drawable.air_dashboard_outdoor_chongqing_50), AQI_HUNDRED_CHONHGING(
			302, R.drawable.air_dashboard_outdoor_chongqing_100), AQI_ONE_FIFTY_CHONHGING(
			303, R.drawable.air_dashboard_outdoor_chongqing_150), AQI_TWO_HUNDRED_CHONHGING(
			304, R.drawable.air_dashboard_outdoor_chongqing_200), AQI_THREE_HUNDRED_CHONHGING(
			305, R.drawable.air_dashboard_outdoor_chongqing_300), AQI_FIVE_HUNDRED_CHONHGING(
			306, R.drawable.air_dashboard_outdoor_chongqing_500),

	AQI_NONE_GUANGZHOU(400, R.drawable.air_dashboard_outdoor_guangzhou_50), AQI_FIFTY_GUANGZHOU(
			401, R.drawable.air_dashboard_outdoor_guangzhou_50), AQI_HUNDRED_GUANGZHOU(
			402, R.drawable.air_dashboard_outdoor_guangzhou_100), AQI_ONE_FIFTY_GUANGZHOU(
			403, R.drawable.air_dashboard_outdoor_guangzhou_150), AQI_TWO_HUNDRED_GUANGZHOU(
			404, R.drawable.air_dashboard_outdoor_guangzhou_200), AQI_THREE_HUNDRED_GUANGZHOU(
			405, R.drawable.air_dashboard_outdoor_guangzhou_300), AQI_FIVE_HUNDRED_GUANGZHOU(
			406, R.drawable.air_dashboard_outdoor_guangzhou_500),

	AQI_NONE_GENERIC(0, R.drawable.air_dashboard_outdoor_generic_city_50), AQI_FIFTY_GENERIC(
			1, R.drawable.air_dashboard_outdoor_generic_city_50), AQI_HUNDRED_GENERIC(
			2, R.drawable.air_dashboard_outdoor_generic_city_100), AQI_ONE_FIFTY_GENERIC(
			3, R.drawable.air_dashboard_outdoor_generic_city_150), AQI_TWO_HUNDRED_GENERIC(
			4, R.drawable.air_dashboard_outdoor_generic_city_200), AQI_THREE_HUNDRED_GENERIC(
			5, R.drawable.air_dashboard_outdoor_generic_city_300), AQI_FIVE_HUNDRED_GENERIC(
			6, R.drawable.air_dashboard_outdoor_generic_city_500);

	private int aqi;
	private int resoutceID;

	private OutdoorImage(int aqi, int resoutceID) {
		this.aqi = aqi;
		this.resoutceID = resoutceID;
	}

	public static int valueOf(String areaId, int aqi) {
		int resId = initializeAQIValue(areaId);

		if (aqi >= 0 && aqi <= 50) {
			resId = resId + 1;
		} else if (aqi > 50 && aqi <= 100) {
			resId = resId + 2;
		} else if (aqi > 100 && aqi <= 150) {
			resId = resId + 3;
		} else if (aqi > 150 && aqi <= 200) {
			resId = resId + 4;
		} else if (resId > 200 && aqi <= 300) {
			resId = resId + 5;
		} else if (aqi > 300) {
			resId = resId + 6;
		} else {
			resId = 0;
		}

		for (OutdoorImage info : OutdoorImage.values()) {
			if (info.aqi == resId) {
				return info.resoutceID;
			}
		}
		return AQI_NONE_GENERIC.resoutceID;
	}

	private static int initializeAQIValue(String areaId) {
		if (areaId.equals("101020100") || areaId.equalsIgnoreCase("Shanghai")) {
			// Shanghai
			return 100;
		} else if (areaId.equals("101010100") || areaId.equalsIgnoreCase("Beijing")) {
			// Beijing
			return 200;
		} else if (areaId.equals("101040100") || areaId.equalsIgnoreCase("Chongqing")) {
			// Chongqing
			return 300;
		} else if (areaId.equals("101280101") || areaId.equalsIgnoreCase("Guangzhou")) {
			// Guangzhou
			return 400;
		} else {
			// Others
			return 0;
		}
	}

}
