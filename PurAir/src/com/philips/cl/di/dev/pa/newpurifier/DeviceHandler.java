package com.philips.cl.di.dev.pa.newpurifier;

import javax.net.ssl.HttpsURLConnection;


import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.TaskPutDeviceDetails;
import com.philips.cl.di.dev.pa.scheduler.SchedulerConstants.SCHEDULE_TYPE;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class DeviceHandler implements ServerResponseListener {
	
	private SubscriptionEventListener mListener;
	
	public DeviceHandler(SubscriptionEventListener listener) {
		mListener = listener;
	}

	public void setPurifierDetails(String key, String value, PurAirDevice purifier) {
		if (purifier == null) return;
		ALog.i(ALog.DEVICEHANDLER, "Setting \"" + key + "\" to " + value + " for purifier: " + purifier.getName());
		
		switch (purifier.getConnectionState()) {
		case CONNECTED_LOCALLY: setDeviceDetailsLocally(key, value, purifier); break;
		case CONNECTED_REMOTELY: setDeviceDetailsRemotely(key, value, purifier); break;
		case DISCONNECTED: break;
		}
	}
	
	public void setScheduleDetails(String dataToSend, PurAirDevice purifier,SCHEDULE_TYPE scheduleType, int scheduleNumber) {
		switch (purifier.getConnectionState()) {
		case CONNECTED_LOCALLY: 
			sendScheduleDetailsLocally(dataToSend,scheduleType, purifier,scheduleNumber) ;
			break;
		case CONNECTED_REMOTELY: 
			sendScheduleDetailsViaCPP(dataToSend,scheduleType, purifier.getEui64(),scheduleNumber) ;
			break;
		case DISCONNECTED: break;
		}
		
	}
	
	private void sendScheduleDetailsLocally(String dataToSend, SCHEDULE_TYPE scheduleType, PurAirDevice purifier,int scheduleNumber) {
		String requestType = "GET" ;
		String url = Utils.getPortUrl(Port.SCHEDULES, purifier.getIpAddress()) ;
		switch (scheduleType) {
		case ADD:
			requestType = "POST" ;
			break;
		case DELETE:
			requestType = "DELETE" ;
			url = Utils.getPortUrl(Port.SCHEDULES, purifier.getIpAddress())+"/"+scheduleNumber ;
			break;
		case GET_SCHEDULE_DETAILS:
			url = Utils.getPortUrl(Port.SCHEDULES, purifier.getIpAddress())+"/"+scheduleNumber ;
			break;
		default:
			break;
		}
		TaskPutDeviceDetails addSchedulerTask =
				new TaskPutDeviceDetails(new DISecurity(null).encryptData(dataToSend, purifier), url , this,requestType) ;
			Thread addSchedulerThread = new Thread(addSchedulerTask) ;
			addSchedulerThread.start() ; 
	}

	private void sendScheduleDetailsViaCPP(String dataToSend,SCHEDULE_TYPE scheduleType, String purifierEUI64,int scheduleNumber) {
		switch (scheduleType) {
		case GET:
			CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(JSONBuilder.getPublishEventBuilderForScheduler("","{}") , AppConstants.DI_COMM_REQUEST, AppConstants.GET_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifierEUI64) ;
			break;
		case ADD:
			CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(JSONBuilder.getPublishEventBuilderForAddScheduler(dataToSend), AppConstants.DI_COMM_REQUEST, AppConstants.ADD_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifierEUI64) ;
			break;	
		case DELETE:
			CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(JSONBuilder.getPublishEventBuilderForDeleteScheduler(scheduleNumber), AppConstants.DI_COMM_REQUEST, AppConstants.DEL_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifierEUI64) ;
			break;		
		case GET_SCHEDULE_DETAILS:
			CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(JSONBuilder.getPublishEventBuilderForGetSchedulerDetails(scheduleNumber), AppConstants.DI_COMM_REQUEST, AppConstants.GET_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifierEUI64) ;
			break;		
		default:
			break;
		}
	}
	private void setDeviceDetailsLocally(String key, String value, PurAirDevice purifier) {
		String dataToUpload = JSONBuilder.getDICommBuilder(key, value, purifier) ;
		if(dataToUpload == null || dataToUpload.isEmpty()) return;
		
		ALog.d(ALog.DEVICEHANDLER, "Performing local request");
		TaskPutDeviceDetails statusUpdateTask = new TaskPutDeviceDetails(dataToUpload, Utils.getPortUrl(Port.AIR, purifier.getIpAddress()), this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}
	
	private void setDeviceDetailsRemotely(String key, String value, PurAirDevice purifier) {
		ALog.d(ALog.DEVICEHANDLER, "Performing CPP request");
		String eventData = JSONBuilder.getPublishEventBuilder(key, value) ;
		CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifier.getEui64()) ;
	}
	
	@Override
	public void receiveServerResponse(int responseCode, String encryptedData, String fromIp) {
		ALog.i(ALog.DEVICEHANDLER, "Response: "+ encryptedData);
		if (responseCode!= HttpsURLConnection.HTTP_OK) return;
		
		notifyListener(encryptedData, fromIp) ;
	}

	private void notifyListener(String encryptedData, String fromIp) {
		if (mListener == null) return;
		ALog.d(ALog.DEVICEHANDLER, "notifying listeners of event: " + encryptedData);
		mListener.onLocalEventReceived(encryptedData, fromIp);
	}

}
