package com.philips.cl.di.dev.pa.purifier;

import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.constant.AppConstants.Port;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.datamodel.SessionDto;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;
import com.philips.cl.di.dev.pa.util.Utils;

public class SubscriptionManager implements UDPEventListener, ServerResponseListener {
	
	private static final int LOCAL_SUBSCRIPTIONTIME = 3360; // IN SEC
	private static final int CPP_SUBSCRIPTIONTIME = 120; // IN MIN
	
	private static SubscriptionManager subscriptionManager ;
	
	private SubscriptionEventListener subscriptionEventListener ;
	
	private UDPSocketManager udpManagerThread ;
		
	private SubscriptionManager() {
		udpManagerThread = new UDPSocketManager(this) ;
	}
	
	public static SubscriptionManager getInstance() {
		if( null == subscriptionManager ) {
			subscriptionManager = new SubscriptionManager() ;
		}
		return subscriptionManager ;
	}
	
	public void setSubscriptionListener(SubscriptionEventListener subscriptionEventListener) {
		this.subscriptionEventListener = subscriptionEventListener ;
	}
	
	public void subscribeToPurifierEvents(String purifierCppID, String purifierIp, boolean isLocalSubscription) {
		ALog.d(ALog.SUBSCRIPTION, "Subscribing to Purifier events for purifier: " + purifierCppID + "   " +  (isLocalSubscription ? "(local)" : "(cpp)"));
		String portUrl = Utils.getPortUrl(Port.AIR, purifierIp);
		subscribe(purifierCppID, portUrl, isLocalSubscription);
	}
	
	public void unSubscribeFromPurifierEvents(String purifierCppID, String purifierIp, boolean isLocalSubscription) {
		ALog.d(ALog.SUBSCRIPTION, "Unsubscribing to Purifier events for purifier: " + purifierCppID + "   " +  (isLocalSubscription ? "(local)" : "(cpp)"));
		String portUrl = Utils.getPortUrl(Port.AIR, purifierIp);
		unSubscribe(purifierCppID, portUrl, isLocalSubscription);
	}
 	
	
	public void enableLocalSubscription() {
		ALog.i(ALog.SUBSCRIPTION, "Enabling local subscription (start udp)") ;
		if( udpManagerThread == null ) {
			udpManagerThread = new UDPSocketManager(this) ;
		}
		if( udpManagerThread != null && !udpManagerThread.isAlive() ) {
			udpManagerThread.start() ;
		}
	}
	
	public void disableLocalSubscription() {
		ALog.i(ALog.SUBSCRIPTION, "Disabling local subscription (stop udp)") ;
		if(udpManagerThread != null && udpManagerThread.isAlive() ) {
			udpManagerThread.stopUDPListener() ;
			udpManagerThread = null ;
		}
	}
	
	private void subscribe(String cppID, String url, boolean isLocal) {
		if( isLocal) {
			TaskPutDeviceDetails subscribe = new TaskPutDeviceDetails(JSONBuilder.getDICommBuilderForSubscribe(cppID,LOCAL_SUBSCRIPTIONTIME), url, this,AppConstants.REQUEST_METHOD_POST) ;
			Thread subscibeThread = new Thread(subscribe) ;
			subscibeThread.start();
		}
		else {
			CPPController.getInstance(PurAirApplication.getAppContext()).
			publishEvent(JSONBuilder.getPublishEventBuilderForSubscribe(AppConstants.EVENTSUBSCRIBER_KEY, SessionDto.getInstance().getEui64()), 
					AppConstants.DI_COMM_REQUEST, AppConstants.SUBSCRIBE,SessionDto.getInstance().getEui64(),"", 20,CPP_SUBSCRIPTIONTIME) ;
		}
	}
	
	private void unSubscribe(String cppID,String url, boolean isLocal) {
		if (isLocal) {
			TaskPutDeviceDetails unSubscribe = new TaskPutDeviceDetails(JSONBuilder.getDICommBuilderForSubscribe(cppID,LOCAL_SUBSCRIPTIONTIME), url, this,AppConstants.REQUEST_METHOD_DELETE) ;
			Thread unSubscibeThread = new Thread(unSubscribe) ;
			unSubscibeThread.start() ;
		}
		else {
			CPPController.getInstance(PurAirApplication.getAppContext()).
			publishEvent(JSONBuilder.getPublishEventBuilderForSubscribe(AppConstants.EVENTSUBSCRIBER_KEY, SessionDto.getInstance().getEui64()),
					AppConstants.DI_COMM_REQUEST, AppConstants.UNSUBSCRIBE, SessionDto.getInstance().getEui64(),"", 20, CPP_SUBSCRIPTIONTIME) ;
		}
	}
	
	@Override
	public void onUDPEventReceived(String data) {
		String decryptedData = new DISecurity(null).decryptData(data, AppConstants.deviceId) ;
		if(decryptedData != null ) {
			ALog.i(ALog.SUBSCRIPTION, decryptedData) ;
			subscriptionEventListener.onSubscribeEventOccurred(decryptedData) ;
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		//TODO if response code not 200? retry?
		if( responseCode != HttpURLConnection.HTTP_OK ) {
			ALog.i(ALog.SUBSCRIPTION, "Subscription failed") ;
		}
		else {
			ALog.i(ALog.SUBSCRIPTION, "Subscription successfull") ;
		}
 	}
} 
