package com.philips.cl.di.dev.pa.scheduler;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.R;

public class SchedulerUtil {
	public static final String BOOTSTRAP_KEY_1 = "5b6c58" ;
	public static int getFanspeedItemPosition(String [] values, String selectedValue) {
		if (selectedValue == null || selectedValue.isEmpty()) return -1;
		int selectedIndex = -1;
		for (int i = 0; i < values.length; i++) {
			if (selectedValue.equals(values[i])) {
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
		if (mode.equals("a")) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.auto);
		} else if (mode.equals("s")) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.silent);
		} else if (mode.equals("1")) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.speed1);
		} else if (mode.equals("2")) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.speed2);
		} else if (mode.equals("3")) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.speed3);
		} else if (mode.equals("t")) {
			modeStr = PurAirApplication.getAppContext().getString(R.string.turbo);
		}
		return modeStr;
	}
}
