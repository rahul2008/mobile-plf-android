package com.philips.cl.di.dev.pa.scheduler;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;

public class SchedulerUtil {
	public static final String BOOTSTRAP_KEY_1 = "NWI2YzU4MD" ;
	
	protected static final String fanSpeedArr[] = {AppConstants.FAN_SPEED_AUTO, AppConstants.FAN_SPEED_SILENT,
												AppConstants.FAN_SPEED_ONE, AppConstants.FAN_SPEED_TWO,
												AppConstants.FAN_SPEED_THREE, AppConstants.FAN_SPEED_TURBO, AppConstants.FAN_SPEED_OFF};
	
	public static int getFanspeedItemPosition(String selectedValue) {
		if (selectedValue == null || selectedValue.isEmpty()) return 0;
		int selectedIndex = 0;
		for (int i = 0; i < fanSpeedArr.length; i++) {
			if (selectedValue.equals(fanSpeedArr[i])) {
				selectedIndex = i;
				break;
			}
		}
		return selectedIndex;
	}

	public static boolean[] getSelectedDayList(String[] days, String selectedDay) {
		boolean[] daysSelectedlist = {false, false, false, false, false, false, false};
		if (selectedDay == null || selectedDay.isEmpty()) return daysSelectedlist;
		String daysSplitArr[] = selectedDay.split(",");
		
		for(int i = 0; i < daysSplitArr.length; i++) {
			for (int j = 0; j < days.length; j++) {
				if (daysSplitArr[i].trim().equals(days[j])) {
					daysSelectedlist[j] = true;
					break;
				}
			}
		}
		return daysSelectedlist;
	}
	
	public static String getFanspeedName(String mode) {
	
		String modeStr = mode;
		if (mode.equals(AppConstants.FAN_SPEED_AUTO)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.auto);
		} else if (mode.equals(AppConstants.FAN_SPEED_SILENT)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.silent);
		} else if (mode.equals(AppConstants.FAN_SPEED_ONE)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.speed1);
		} else if (mode.equals(AppConstants.FAN_SPEED_TWO)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.speed2);
		} else if (mode.equals(AppConstants.FAN_SPEED_THREE)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.speed3);
		} else if (mode.equals(AppConstants.FAN_SPEED_TURBO)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.turbo);
		} else if (mode.equals(AppConstants.FAN_SPEED_OFF)) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.fanspeed_off);
		}
		return modeStr;
	}
}
