package com.philips.cl.di.dev.pa.scheduler;

import java.util.HashMap;

public interface SchedulerEditListener {
	void onDeleteSchedule(int position, HashMap<Integer, Boolean> selectedItems);

	void onEditSchedule(int position);
}
