package com.philips.cl.di.dev.pa.util;

import static com.philips.cl.di.dev.pa.util.AppConstants.*;

public class Utils {
	
	/**
	 * Gets the filter status color.
	 * 
	 * @param filterStatusValue
	 *            the filter status value
	 * @return the filter status color
	 */
	public static int getFilterStatusColor(float filterStatusValue) {
		float filterRange = (filterStatusValue / (AppConstants.MAXIMUMFILTER - AppConstants.MINIMUNFILTER)) * 100;
		if (filterRange >= 0 && filterRange <= 25) {
			return AppConstants.COLOR_BAD;
		} else if (filterRange > 25 && filterRange <= 50) {
			return AppConstants.COLOR_FAIR;
		} else if (filterRange > 50 && filterRange <= 75) {
			return AppConstants.COLOR_GOOD;
		} else if (filterRange > 75 && filterRange <= 100) {
			return AppConstants.COLOR_VGOOD;
		}
		return 0;
	}
	
	public static String getFanSpeedText(String fanSpeed) {
		if(FAN_SPEED_SILENT.equals(fanSpeed)) {
			return "Silent";
		} else if(FAN_SPEED_TURBO.equals(fanSpeed)) {
			return "Turbo";
		} else if(FAN_SPEED_AUTO.equals(fanSpeed)) {
			return "Auto";
		} else if(FAN_SPEED_ONE.equals(fanSpeed)) {
			return "1";
		} else if(FAN_SPEED_TWO.equals(fanSpeed)) {
			return "2";
		} else if(FAN_SPEED_THREE.equals(fanSpeed)) {
			return "3";
		} 
		return "";
	}
	
}
