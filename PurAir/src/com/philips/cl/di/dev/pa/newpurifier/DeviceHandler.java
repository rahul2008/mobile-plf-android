package com.philips.cl.di.dev.pa.newpurifier;

import java.net.HttpURLConnection;
import java.util.Hashtable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager.PURIFIER_EVENT;
import com.philips.cl.di.dev.pa.purifier.RoutingStrategy;
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
	private Thread statusUpdateTaskThread ;
	private PurAirDevice purifier ;
	
	private Hashtable<String, String> deviceDetailsTable ;
	private boolean stop;
	
	public DeviceHandler(SubscriptionEventListener listener) {
		mListener = listener ;
		deviceDetailsTable = new Hashtable<String, String>() ;
	}
	
	public DeviceHandler(SubscriptionEventListener listener, PURIFIER_EVENT purifierEvent) {
		this(listener);
		mPurifierEvent = purifierEvent ;
	}
	
	public void setPurifierEvent(PURIFIER_EVENT purifierEvent) {
		mPurifierEvent = purifierEvent ;
	}

	public synchronized void setPurifierDetails(String key, String value, PurAirDevice purifier) {
		if (purifier == null) return;
		this.purifier = purifier;
		deviceDetailsTable.put(key, value) ;
		ALog.i(ALog.DEVICEHANDLER, "Setting \"" + key + "\" to " + value + " for purifier: " + purifier.getName());
		
		if( statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) {
			startUpdateTask(purifier) ;
		}		
	}
	
	private void startUpdateTask(PurAirDevice purifier) {
		ALog.i(ALog.DEVICEHANDLER, "Start update task") ;
		StatusUpdateThread status = new StatusUpdateThread() ;
		statusUpdateTaskThread = new Thread(status) ;
		statusUpdateTaskThread.start() ;
	}
	
	public boolean isDeviceThreadRunning() {
		if(statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) 
			return false ;
		return true;
	}
	
	public void stopDeviceThread() {
		if(statusUpdateTaskThread == null || !statusUpdateTaskThread.isAlive()) return ;
		stop = true ;
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
	
	private String response ;
	
	private class StatusUpdateThread implements Runnable {
		Hashtable<String, String> airPortDetailsTable = new Hashtable<String, String>();
		private RoutingStrategy connection ;
		
		public void run() {
			sendRequest() ;
		}
		private void sendRequest() {
			if( deviceDetailsTable.size() == 0 ) {
				messageHandler.sendEmptyMessage(0) ;
				return ;
			}
			if( !stop ) {				
				this.airPortDetailsTable.putAll(deviceDetailsTable) ;
				deviceDetailsTable.clear() ;
				connection = RoutingStrategy.getConnection(purifier, this.airPortDetailsTable) ;
				if( connection == null) return ;
				response = connection.setPurifierDetails() ;
				sendRequest() ;
			}
		}
	}
	
	private void notifyListener() {
		if( stop == true ) return ;
		if( response != null && !response.isEmpty() ) {
			if( purifier.getConnectionState() == ConnectionState.CONNECTED_LOCALLY) {
				mListener.onLocalEventReceived(response, purifier.getIpAddress()) ;
			}
			else if( purifier.getConnectionState() == ConnectionState.CONNECTED_REMOTELY) {
				mListener.onRemoteEventReceived(response, purifier.getEui64()) ;
			}
		}
		else {
			mListener.onLocalEventLost(mPurifierEvent) ;
		}
	}
	
	private Handler messageHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if( msg.what == 0)
				notifyListener() ;
		};
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
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
		case EDIT:
			CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(JSONBuilder.getPublishEventBuilderForEditScheduler(dataToSend,scheduleNumber), AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifierEUI64) ;
			break ;
		default:
			break;
		}
	}
}


//private void setDeviceDetailsLocally(String key, String value, PurAirDevice purifier) {
//String dataToUpload = JSONBuilder.getDICommBuilder(key, value, purifier) ;
//if(dataToUpload == null || dataToUpload.isEmpty()) return;
//
//ALog.d(ALog.DEVICEHANDLER, "Performing local request");
//if( statusUpdateTaskThread != null && statusUpdateTaskThread.isAlive()) {
//	
//}
//else {
//	TaskPutDeviceDetails statusUpdateTask = new TaskPutDeviceDetails(dataToUpload, Utils.getPortUrl(Port.AIR, purifier.getIpAddress()), this) ;
//	statusUpdateTaskThread = new Thread(statusUpdateTask) ;
//	statusUpdateTaskThread.start() ;
//}
//}

//private void setDeviceDetailsRemotely(String key, String value, PurAirDevice purifier) {
//ALog.d(ALog.DEVICEHANDLER, "Performing CPP request");
//String eventData = JSONBuilder.getPublishEventBuilder(key, value) ;
//CPPController.getInstance(PurAirApplication.getAppContext()).publishEvent(eventData,AppConstants.DI_COMM_REQUEST, AppConstants.PUT_PROPS, SessionDto.getInstance().getAppEui64(), "", 20, 120, purifier.getEui64()) ;
//}

