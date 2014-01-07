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
	
	public static int getPreFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 96) {
			return COLOR_GOOD;
		} else if ( filterStatusValue >= 96 && filterStatusValue < 112) {
			return COLOR_FAIR;
		} else {
			return COLOR_BAD;
		}
	}
	
	public static String getPreFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 96) {
			return "Normal operation";
		} else if ( filterStatusValue >= 96 && filterStatusValue < 112) {
			return "Clean soon";
		} else {
			return "Clean now";
		}
	}
	
	public static int getMultiCareFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 784) {
			return COLOR_GOOD;
		} else if ( filterStatusValue >= 784 && filterStatusValue < 840) {
			return COLOR_FAIR;
		} else {
			return COLOR_BAD;
		}
	}
	
	public static String getMultiCareFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 784) {
			return "Normal operation";
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return "Change soon";
		} else if(filterStatusValue >= 840 && filterStatusValue < 960){
			return "Change now";
		} else {
			return "Filter lock";
		}
	}
	
	public static int getActiveCarbonFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return COLOR_GOOD;
		} else if ( filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return COLOR_FAIR;
		} else {
			return COLOR_BAD;
		}
	}
	
	public static String getActiveCarbonFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return "Normal operation";
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return "Change soon";
		} else if(filterStatusValue >= 2760 && filterStatusValue < 2880){
			return "Change now";
		} else {
			return "Filter lock";
		}
	}
	
	public static int getHEPAFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return COLOR_GOOD;
		} else if ( filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return COLOR_FAIR;
		} else {
			return COLOR_BAD;
		}
	}
	
	public static String getHEPAFilterFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return "Normal operation";
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return "Change soon";
		} else if(filterStatusValue >= 2760 && filterStatusValue < 2880){
			return "Change now";
		} else {
			return "Filter lock";
		}
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
