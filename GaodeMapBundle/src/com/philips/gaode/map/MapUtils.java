package com.philips.gaode.map;

/**
 * 
 * MapUtils class is base utility class for GAODE MAP implementation. 
 * Author : Ritesh.jha@philips.com 
 * Date : 10 Oct 2014
 * 
 */
public class MapUtils {
	private static MapUtils mapUtils = null;

	private MapUtils() {

	}

	public static MapUtils getMapUtilsInstace() {
		if (mapUtils == null) {
			mapUtils = new MapUtils();
		}
		return mapUtils;
	}

	/*
	 * Accepts AQI values and on that basis returns marker image.
	 * 
	 * Argument : aqi represents AQI value. 
	 * 
	 * Argument : iconOval represents the
	 * drawable type
	 */
	public int getAqiPointerImageResId(int aqi, boolean iconOval) {

		if (!iconOval) {
			if (aqi >= 0 && aqi <= 50) {
				return R.drawable.map_circle_6;
			} else if (aqi > 50 && aqi <= 100) {
				return R.drawable.map_circle_5;
			} else if (aqi > 100 && aqi <= 150) {
				return R.drawable.map_circle_4;
			} else if (aqi > 150 && aqi <= 200) {
				return R.drawable.map_circle_3;
			} else if (aqi > 200 && aqi <= 300) {
				return R.drawable.map_circle_2;
			} else if (aqi > 300) {
				return R.drawable.map_circle_1;
			}
		} else {
			if (aqi >= 0 && aqi <= 50) {
				return R.drawable.map_oval_6;
			} else if (aqi > 50 && aqi <= 100) {
				return R.drawable.map_oval_5;
			} else if (aqi > 100 && aqi <= 150) {
				return R.drawable.map_oval_4;
			} else if (aqi > 150 && aqi <= 200) {
				return R.drawable.map_oval_3;
			} else if (aqi > 200 && aqi <= 300) {
				return R.drawable.map_oval_2;
			} else if (aqi > 300) {
				return R.drawable.map_oval_1;
			}
		}
		return R.drawable.map_circle_6;
	}
}