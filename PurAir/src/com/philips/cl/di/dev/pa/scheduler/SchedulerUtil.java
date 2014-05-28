package com.philips.cl.di.dev.pa.scheduler;

public class SchedulerUtil {
	
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
}
