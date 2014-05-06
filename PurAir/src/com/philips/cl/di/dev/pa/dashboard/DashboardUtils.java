package com.philips.cl.di.dev.pa.dashboard;

import android.content.Context;
import android.widget.ImageView;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.ALog;

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
	
	public static int getAqiPointerBackgroundId(int indoorAQI) {
		int resourceId = R.drawable.blue_circle_with_arrow_2x;
		if(indoorAQI >= 0 && indoorAQI <= 14) {
			resourceId = R.drawable.blue_circle_with_arrow_2x;
		} else if (indoorAQI > 14 && indoorAQI <= 23) {
			resourceId = R.drawable.light_pink_circle_arrow1_2x;
		} else if (indoorAQI > 23 && indoorAQI <= 35) {
			resourceId = R.drawable.red_circle_arrow_2x;
		} else if (indoorAQI > 35) {
			resourceId = R.drawable.light_red_circle_arrow_2x;
		}
		
		return resourceId;
	}
}
