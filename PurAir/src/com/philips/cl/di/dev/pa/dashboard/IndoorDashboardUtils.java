package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.datamodel.AirPortInfo;
import com.philips.cl.di.dev.pa.util.Utils;

public class IndoorDashboardUtils {
	
	public static int getFanSpeedText(String fanSpeed) {
	
		if(fanSpeed == null || fanSpeed.isEmpty()) {
			return R.string.empty_string;
		}
		
		if(fanSpeed.equals(AppConstants.FAN_SPEED_SILENT)) {
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
	
	public static int getAqiPointerBackgroundId(int indoorAQI) {
		if(indoorAQI >= 0 && indoorAQI <= 14) {
			return R.drawable.blue_circle_with_arrow_2x;
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			return R.drawable.light_pink_circle_arrow1_2x;
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			return R.drawable.red_circle_arrow_2x;
		} else if (indoorAQI > 35) {
			return R.drawable.light_red_circle_arrow_2x;
		}
		
		return R.drawable.blue_circle_with_arrow_2x;
	}

	public static String getFilterStatus(AirPortInfo airPurifierEventDto) {
		String filterStatus = "-";
		if (airPurifierEventDto != null) {
			Context context=PurAirApplication.getAppContext();
			filterStatus = context.getString(R.string.good);
			String preFilterStatus = Utils.getPreFilterStatusText(airPurifierEventDto.getFilterStatus1());
			String multiCareFilterStatus = Utils.getMultiCareFilterStatusText(airPurifierEventDto.getFilterStatus2());
			String activeFilterStatus = Utils.getActiveCarbonFilterStatusText(airPurifierEventDto.getFilterStatus3());
			String hepaFilterStatus = Utils.getHEPAFilterFilterStatusText(airPurifierEventDto.getFilterStatus4());

			if (multiCareFilterStatus.equals(AppConstants.ACT_NOW)
					|| activeFilterStatus.equals(AppConstants.ACT_NOW)
					|| hepaFilterStatus.equals(AppConstants.ACT_NOW)) {
				filterStatus = context.getString(R.string.change_now);
			} else if (preFilterStatus.equals(AppConstants.CLEAN_NOW)) {
				filterStatus = context.getString(R.string.clean_now);
			} else if (preFilterStatus.equals(AppConstants.CLEAN_SOON)) {
				filterStatus =context.getString(R.string.clean_soon);
			} else if (multiCareFilterStatus.equals(AppConstants.ACT_SOON)
					|| activeFilterStatus.equals(AppConstants.ACT_SOON)
					|| hepaFilterStatus.equals(AppConstants.ACT_SOON)) {
				filterStatus = context.getString(R.string.change_now);
			} else if (multiCareFilterStatus.equals(AppConstants.FILTER_LOCK)
					|| activeFilterStatus.equals(AppConstants.FILTER_LOCK)
					|| hepaFilterStatus.equals(AppConstants.FILTER_LOCK)) {
				filterStatus = context.getString(R.string.filter_lock);
			}
		}
		return filterStatus;
	}

	public static float getAqiPointerRotation(int indoorAqi) {
		float rotation = 0.0f;
		float indoorAqiFloat = (float) indoorAqi;
		float addFloat = 0F;
		if(indoorAqi >= 0 && indoorAqi <= 14) {
			rotation = indoorAqiFloat * 1.9f;
		} else if (indoorAqi > 14 && indoorAqi <= 23) {
			indoorAqiFloat -= 14;
			addFloat = indoorAqiFloat * 3.25f;
			rotation = -4 + 27.0f + addFloat;
		} else if (indoorAqi > 23 && indoorAqi <= 35) {
			indoorAqiFloat -= 23;
			addFloat = indoorAqiFloat * 2.33f;
			rotation = -3 + 56.0f + addFloat;
		} else if (indoorAqi > 35) {
			indoorAqiFloat -= 35;
			addFloat = indoorAqiFloat * 1.0f;
			rotation = 86.0f + addFloat ;
			if(rotation > 302) {
				rotation = 302;
			}
		}
		return rotation;
	}
	
	public static int getAqiTitle(int indoorAqi) {
		if(indoorAqi >= 0 && indoorAqi <= 14) {
			return R.string.good;
		} else if(indoorAqi > 14 && indoorAqi <= 23) {
			return R.string.moderate;
		} else if(indoorAqi > 23 && indoorAqi <= 35) {
			return R.string.unhealthy;
		} else if(indoorAqi > 35) {
			return R.string.very_unhealthy_split;
		}
		return R.string.empty_string;
	}
	
	public static int getAqiSummary(int indoorAqi) {
		if(indoorAqi >= 0 && indoorAqi <= 14) {
			return R.string.indoor_aqi_good_tip1;
		} else if(indoorAqi > 14 && indoorAqi <= 23) {
			return R.string.indoor_aqi_moderate_tip1;
		} else if(indoorAqi > 23 && indoorAqi <= 35) {
			return R.string.indoor_aqi_unhealthy_tip1;
		} else if(indoorAqi > 35) {
			return R.string.indoor_aqi_very_unhealthy_tip1;
		}
		return R.string.empty_string;
	}
}
