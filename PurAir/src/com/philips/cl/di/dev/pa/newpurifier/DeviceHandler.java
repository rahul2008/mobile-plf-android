package com.philips.cl.di.dev.pa.newpurifier;

import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
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
	private PURIFIER_EVENT mPurifierEvent ;
	
	public DeviceHandler(SubscriptionEventListener listener) {
		mListener = listener ;
	}
	
	public DeviceHandler(SubscriptionEventListener listener, PURIFIER_EVENT purifierEvent) {
		this(listener);
		mPurifierEvent = purifierEvent ;
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
		String requestType = AppConstants.REQUEST_METHOD_GET ;
		String url = Utils.getPortUrl(Port.SCHEDULES, purifier.getIpAddress()) ;
		switch (scheduleType) {
		case ADD:
			requestType = AppConstants.REQUEST_METHOD_POST ;
			break;
		case DELETE:
			requestType = AppConstants.REQUEST_METHOD_DELETE ;
			url =  Utils.getScheduleDetailsUrl(purifier.getIpAddress(),scheduleNumber);
			break;
		case GET_SCHEDULE_DETAILS:
			url = Utils.getScheduleDetailsUrl(purifier.getIpAddress(),scheduleNumber);
			break;
		case EDIT:
			requestType = AppConstants.REQUEST_METHOD_PUT ;
			url =  Utils.getScheduleDetailsUrl(purifier.getIpAddress(),scheduleNumber);
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
		case EDIT:
			CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(JSONBuilder.getPublishEventBuilderForEditScheduler(dataToSend,scheduleNumber), AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifierEUI64) ;
			break ;
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
		ALog.i(ALog.DEVICEHANDLER, "Receive response");
		notifyListener(responseCode,encryptedData, fromIp) ;
	}

	private void notifyListener(int responseCode, String encryptedData, String fromIp) {
		ALog.i(ALog.DEVICEHANDLER, "Response Code: "+responseCode) ;
		if (mListener == null) return;
		if(responseCode != HttpURLConnection.HTTP_OK) {
			mListener.onLocalEventLost(mPurifierEvent) ; 
		}
		else {
			mListener.onLocalEventReceived(encryptedData, fromIp) ;
		}
	}
}
