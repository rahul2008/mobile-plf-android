package com.philips.cl.di.dev.pa.util;

import android.content.Context;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.util.AppConstants;
import com.philips.cl.di.dev.pa.dto.AirPurifierEventDto;

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
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 96 && filterStatusValue < 112) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getPreFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 96) {
			return AppConstants.GOOD;
		} else if ( filterStatusValue >= 96 && filterStatusValue < 112) {
			return AppConstants.CLEAN_SOON;
		} else {
			return AppConstants.CLEAN_NOW;
		}
	}
	
	public static int getMultiCareFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 784) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 784 && filterStatusValue < 840) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getMultiCareFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 784) {
			return AppConstants.GOOD ;
		} else if (filterStatusValue >= 784 && filterStatusValue < 840) {
			return AppConstants.ACT_SOON ;
		} else if(filterStatusValue >= 840 && filterStatusValue < 960){
			return AppConstants.ACT_NOW ;
		} else {
			return AppConstants.FILTER_LOCK ;
		}
	}
	
	public static int getActiveCarbonFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getActiveCarbonFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return  AppConstants.GOOD ;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.ACT_SOON ;
		} else if(filterStatusValue >= 2760 && filterStatusValue < 2880){
			return AppConstants.ACT_NOW ;
		} else {
			return AppConstants.FILTER_LOCK ;
		}
	}
	
	public static int getHEPAFilterStatusColour(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return AppConstants.COLOR_GOOD;
		} else if ( filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.COLOR_FAIR;
		} else {
			return AppConstants.COLOR_BAD;
		}
	}
	
	public static String getHEPAFilterFilterStatusText(int filterStatusValue) {
		if(filterStatusValue < 2704) {
			return AppConstants.GOOD;
		} else if (filterStatusValue >= 2704 && filterStatusValue < 2760) {
			return AppConstants.ACT_SOON;
		} else if(filterStatusValue >= 2760 && filterStatusValue < 2880){
			return AppConstants.ACT_NOW;
		} else {
			return AppConstants.FILTER_LOCK;
		}
	}
	
	public static String getFanSpeedText(String fanSpeed) {
		if(AppConstants.FAN_SPEED_SILENT.equals(fanSpeed)) {
			return "Silent";
		} else if(AppConstants.FAN_SPEED_TURBO.equals(fanSpeed)) {
			return "Turbo";
		} else if(AppConstants.FAN_SPEED_AUTO.equals(fanSpeed)) {
			return "Auto";
		} else if(AppConstants.FAN_SPEED_ONE.equals(fanSpeed)) {
			return "1";
		} else if(AppConstants.FAN_SPEED_TWO.equals(fanSpeed)) {
			return "2";
		} else if(AppConstants.FAN_SPEED_THREE.equals(fanSpeed)) {
			return "3";
		} 
		return "";
	}
	
	public static String getMode(String fanSpeed, Context context) {
		String mode = "";
		if(AppConstants.FAN_SPEED_SILENT.equals(fanSpeed)) {
			mode = context.getString(R.string.silent);
		} else if(AppConstants.FAN_SPEED_TURBO.equals(fanSpeed)) {
			mode = context.getString(R.string.turbo);
		} else if(AppConstants.FAN_SPEED_AUTO.equals(fanSpeed)) {
			mode = context.getString(R.string.auto);
		} else if(AppConstants.FAN_SPEED_ONE.equals(fanSpeed)) {
			mode = context.getString(R.string.speed1);
		} else if(AppConstants.FAN_SPEED_TWO.equals(fanSpeed)) {
			mode = context.getString(R.string.speed2);
		} else if(AppConstants.FAN_SPEED_THREE.equals(fanSpeed)) {
			mode = context.getString(R.string.speed3);
		}
		return mode;
	}

	public static String getFilterStatusForDashboard(
			AirPurifierEventDto airPurifierEventDto) {
		String filterStatus = AppConstants.GOOD ;
		if ( airPurifierEventDto != null ) {
			String preFilterStatus = getPreFilterStatusText(airPurifierEventDto.getFilterStatus1()) ;
			String multiCareFilterStatus = getMultiCareFilterStatusText(airPurifierEventDto.getFilterStatus2()) ;
			String activeFilterStatus = getActiveCarbonFilterStatusText(airPurifierEventDto.getFilterStatus3()) ;
			String hepaFilterStatus = getHEPAFilterFilterStatusText(airPurifierEventDto.getFilterStatus4()) ;
			
			if ( multiCareFilterStatus.equals(AppConstants.ACT_NOW) ||
					activeFilterStatus.equals(AppConstants.ACT_NOW) ||
					hepaFilterStatus.equals(AppConstants.ACT_NOW)) {
				filterStatus = AppConstants.ACT_NOW ;
			}
			else if ( preFilterStatus.equals(AppConstants.CLEAN_NOW)) {
				filterStatus = AppConstants.CLEAN_NOW ;
			}
			else if ( preFilterStatus.equals(AppConstants.CLEAN_SOON)) {
				filterStatus = AppConstants.CLEAN_SOON ;
			}
			else if (multiCareFilterStatus.equals(AppConstants.ACT_SOON) ||
					activeFilterStatus.equals(AppConstants.ACT_SOON) ||
					hepaFilterStatus.equals(AppConstants.ACT_SOON)) {
				filterStatus = AppConstants.ACT_SOON ;
			}
			else if (multiCareFilterStatus.equals(AppConstants.FILTER_LOCK) ||
					activeFilterStatus.equals(AppConstants.FILTER_LOCK) ||
					hepaFilterStatus.equals(AppConstants.FILTER_LOCK)) {
				filterStatus = AppConstants.FILTER_LOCK ;
			}
		}
		return filterStatus ;
	}
	
	public static int getIndoorAQIMessage(int aqi) {
		if(aqi >= 0 && aqi <= 50) {
			return R.string.very_healthy_msg_indoor;
		} else if(aqi > 50 && aqi <= 100) {
			return R.string.healthy_msg_indoor;
		} else if(aqi > 100 && aqi <= 150) {
			return R.string.slightly_polluted_msg_indoor;
		} else if(aqi > 150 && aqi <= 200) {
			return R.string.moderately_polluted_msg_indoor;
		} else if(aqi > 200 && aqi <= 300) {
			return R.string.unhealthy_msg_indoor;
		} else if(aqi > 300 && aqi <= 1000) {
			return R.string.hazardous_msg_indoor;
		}
		return R.string.n_a;
	}
	
}
