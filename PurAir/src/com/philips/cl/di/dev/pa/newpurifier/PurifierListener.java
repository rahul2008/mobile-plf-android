package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;

import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager.PurifierEvent;
import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;

public interface PurifierListener {

	public abstract void notifyAirPurifierEventListeners();

	public abstract void notifyFirmwareEventListeners();

	public abstract void notifyScheduleListenerForSingleSchedule(
			SchedulePortInfo schedulePortInfo);

	public abstract void notifyScheduleListenerForScheduleList(
			List<SchedulePortInfo> schedulePortInfoList);

	public abstract void notifyScheduleListenerForErrorOccured(int errorType);

	public abstract void notifyAirPurifierEventListenersErrorOccurred(
			PurifierEvent purifierEvent);

}