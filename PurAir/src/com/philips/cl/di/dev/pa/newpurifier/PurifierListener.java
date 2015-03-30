package com.philips.cl.di.dev.pa.newpurifier;

import java.util.List;

import com.philips.cl.di.dev.pa.scheduler.SchedulePortInfo;
import com.philips.cl.di.dicomm.communication.Error;

public interface PurifierListener {

	void notifyAirPurifierEventListeners();

	void notifyFirmwareEventListeners();

	void notifyScheduleListenerForSingleSchedule(
			SchedulePortInfo schedulePortInfo);

	void notifyScheduleListenerForScheduleList(
			List<SchedulePortInfo> schedulePortInfoList);

	void notifyScheduleListenerForErrorOccured(int errorType);

	void notifyAirPurifierEventListenersErrorOccurred(
			Error.PurifierEvent purifierEvent);

}