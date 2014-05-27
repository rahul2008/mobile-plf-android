package com.philips.cl.di.dev.pa.newpurifier;

import javax.net.ssl.HttpsURLConnection;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.TaskPutDeviceDetails;
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
		ALog.d(ALog.DEVICEHANDLER, "Setting " + key + " to " + value + " for purifier: " + purifier.getName());
		
		switch (purifier.getConnectionState()) {
		case CONNECTED_LOCALLY: setDeviceDetailsLocally(key, value, purifier); break;
		case CONNECTED_REMOTELY: setDeviceDetailsRemotely(key, value, purifier); break;
		case DISCONNECTED: break;
		}
	}
	
	private void setDeviceDetailsLocally(String key, String value, PurAirDevice purifier) {
		String dataToUpload = JSONBuilder.getDICommBuilder(key, value, purifier) ;
		if(dataToUpload == null || dataToUpload.isEmpty()) return;
		
		TaskPutDeviceDetails statusUpdateTask = new TaskPutDeviceDetails(dataToUpload, Utils.getPortUrl(Port.AIR, purifier.getIpAddress()), this) ;
		Thread statusUpdateTaskThread = new Thread(statusUpdateTask) ;
		statusUpdateTaskThread.start() ;
	}
	
	private void setDeviceDetailsRemotely(String key, String value, PurAirDevice purifier) {
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
