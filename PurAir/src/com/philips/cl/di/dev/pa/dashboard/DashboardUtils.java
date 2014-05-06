package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;

public class DashboardUtils {
	
	public static String getFanSpeedText(String fanSpeed) {
		Context ctx = PurAirApplication.getAppContext();
		String fanMode = "";
		if (fanSpeed == null || fanSpeed.isEmpty()) {
			return fanMode;
		}
	
		if(fanSpeed.equals(AppConstants.FAN_SPEED_SILENT)) {
			fanMode= ctx.getString(R.string.silent);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_AUTO)) {
			fanMode= ctx.getString(R.string.auto);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_TURBO)) {
			fanMode= ctx.getString(R.string.turbo);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_ONE)) {
			fanMode= ctx.getString(R.string.one);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_TWO)) {
			fanMode= ctx.getString(R.string.two);
		} else if (fanSpeed.equals(AppConstants.FAN_SPEED_THREE)) {
			fanMode= ctx.getString(R.string.three);
		}
		
		return fanMode;
	}
}
