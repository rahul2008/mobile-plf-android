package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;

import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dicomm.communication.Error;

public interface PurifierListener {

	public abstract void notifyAirPurifierEventListeners();

	public abstract void notifyFirmwareEventListeners();

	public abstract void notifyScheduleListenerForSingleSchedule(
			SchedulePortInfo schedulePortInfo);

	public abstract void notifyScheduleListenerForScheduleList(
			List<SchedulePortInfo> schedulePortInfoList);

	public abstract void notifyScheduleListenerForErrorOccured(int errorType);

	public abstract void notifyAirPurifierEventListenersErrorOccurred(
			Error.PurifierEvent purifierEvent);

}