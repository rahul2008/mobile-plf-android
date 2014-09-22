package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;

public class IndoorDashboardUtils {

	public static int getFanSpeedText(String fanSpeed) {

		if (fanSpeed == null || fanSpeed.isEmpty()) {
			return R.string.empty_string;
		}

		if (fanSpeed.equals(AppConstants.FAN_SPEED_SILENT)) {
			return R.string.silent;
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_AUTO)) {
			return R.string.auto;
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_TURBO)) {
			return R.string.turbo;
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_ONE)) {
			return R.string.speed1;
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_TWO)) {
			return R.string.speed2;
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_THREE)) {
			return R.string.speed3;
		}

		return R.string.empty_string;
	}

	public static String getFilterStatus(AirPortInfo airPurifierEventDto) {
		String filterStatus = "-";
		if (airPurifierEventDto != null) {
			Context context = PurAirApplication.getAppContext();
			filterStatus = context.getString(R.string.good);
			//Simplified logic to calculate filter status
			int preFilter = airPurifierEventDto.getPreFilterStatus();
			int multiCareFilter = airPurifierEventDto.getMulticareFilterStatus();
			int activeFilter = airPurifierEventDto.getActiveFilterStatus();
			int hepaFilter = airPurifierEventDto.getHepaFilterStatus();
			
			if ( (multiCareFilter >= 840 && multiCareFilter < 960)
					|| (activeFilter >= 2760 && activeFilter < 2880)
					|| (hepaFilter >= 2760 && hepaFilter < 2880)) {
				filterStatus = context.getString(R.string.change_now);
			} else if (preFilter >= 112) {
				filterStatus = context.getString(R.string.clean_now);
			} else if (multiCareFilter >= 960 || activeFilter >= 2880 || hepaFilter >= 2880) {
				filterStatus = context.getString(R.string.filter_lock);
			}
		}
		return filterStatus;
	}

	public static int getAqiPointerMarker(int indoorAQI) {
		if (indoorAQI >= 0 && indoorAQI <= 14) {
			return R.drawable.air_dashboard_indoor_map_good;
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			return R.drawable.air_dashboard_indoor_map_moderate;
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			return R.drawable.air_dashboard_indoor_map_unhealthy;
		} else if (indoorAQI > 35) {
			return R.drawable.air_dashboard_indoor_map_very_unhealthy;
		}

		return R.drawable.air_dashboard_indoor_map_good;
	}
	
	public static int getAqiTitle(int indoorAqi) {
		if (indoorAqi >= 0 && indoorAqi <= 14) {
			return R.string.good_indoor;
		} else if (indoorAqi > 14 && indoorAqi <= 23) {
			return R.string.moderate_indoor;
		} else if (indoorAqi > 23 && indoorAqi <= 35) {
			return R.string.unhealthy_indoor;
		} else if (indoorAqi > 35) {
			return R.string.very_unhealthy_split_indoor;
		}
		return R.string.empty_string;
	}

	public static DashboardAPL getDashboardAPL(int indoorAqi) {
		int pointerBackground;
		int pointerRotation;
		int title;
		int summary;
		
		float step = 31;
		float start = 105;
		
		if(indoorAqi <= 0) {
			pointerBackground = R.drawable.blue_circle_with_arrow_2x;
			pointerRotation = 0;
			title = R.string.empty_string;
			summary = R.string.empty_string;
		} else if (indoorAqi >= 0 && indoorAqi <= 14) {
			pointerBackground = R.drawable.blue_circle_with_arrow_2x;
			pointerRotation = 14;
			title = R.string.good_indoor; 
			summary = R.string.indoor_aqi_good_tip1;
		} else if (indoorAqi > 14 && indoorAqi <= 23) {
			pointerBackground = R.drawable.light_pink_circle_arrow1_2x;
			pointerRotation = 40;
			title = R.string.moderate_indoor; 
			summary = R.string.indoor_aqi_moderate_tip1;
		} else if (indoorAqi > 23 && indoorAqi <= 35) {
			pointerBackground = R.drawable.red_circle_arrow_2x;
			pointerRotation = 68;
			title = R.string.unhealthy_indoor; 
			summary = R.string.indoor_aqi_unhealthy_tip1;
		} else if (indoorAqi > 35 && indoorAqi <= 95) {
			pointerBackground = R.drawable.light_red_circle_arrow_2x;
			pointerRotation = (int) (start + (step * ((indoorAqi - 36) / 10)));
			title = R.string.very_unhealthy_split_indoor;
			summary = R.string.indoor_aqi_very_unhealthy_tip1;
		} else {
			pointerBackground = R.drawable.light_red_circle_arrow_2x;
			pointerRotation = (int) (start + (step * 6));
			title = R.string.very_unhealthy_split_indoor;
			summary = R.string.indoor_aqi_very_unhealthy_tip1;
		}
		
		DashboardAPL apl = new DashboardAPL(pointerBackground, pointerRotation, title, summary);
		
		return apl;
	}
}
