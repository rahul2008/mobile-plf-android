package com.philips.cl.di.dev.pa.scheduler;

import java.util.List;

public interface SchedulePortListener {
	
	public void onSchedulesReceived(List<SchedulePortInfo> scheduleList) ;
	public void onScheduleReceived(SchedulePortInfo schedule) ;
	public void onError(int errorType) ;
	
}
