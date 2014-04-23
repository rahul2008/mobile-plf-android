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
	
	private static SubscriptionManager mInstance ;
	
	private SubscriptionEventListener subscriptionEventListener ;
	
	private UDPSocketManager udpManagerThread ;
		
	private SubscriptionManager() {
		udpManagerThread = new UDPSocketManager(this) ;
	}
	
	public static SubscriptionManager getInstance() {
		if( null == mInstance ) {
			mInstance = new SubscriptionManager() ;
		}
		return mInstance ;
	}
	
	public void setSubscriptionListener(SubscriptionEventListener subscriptionEventListener) {
		this.subscriptionEventListener = subscriptionEventListener ;
	}
	
	public void subscribeToPurifierEvents(String bootId, String purifierIp, boolean isLocalSubscription) {
		ALog.d(ALog.SUBSCRIPTION, "Subscribing to Purifier events for purifier: " + bootId + "   " +  (isLocalSubscription ? "(local)" : "(cpp)"));
		String portUrl = Utils.getPortUrl(Port.AIR, purifierIp);
		subscribe(bootId, portUrl, isLocalSubscription);
	}
	
	public void unSubscribeFromPurifierEvents(String bootId, String purifierIp, boolean isLocalSubscription) {
		ALog.d(ALog.SUBSCRIPTION, "Unsubscribing to Purifier events for purifier: " + bootId + "   " +  (isLocalSubscription ? "(local)" : "(cpp)"));
		String portUrl = Utils.getPortUrl(Port.AIR, purifierIp);
		unSubscribe(bootId, portUrl, isLocalSubscription);
	}
	
	public void subscribeToFirmwareEvents(String bootId, String purifierIp) {
		ALog.d(ALog.SUBSCRIPTION, "Subscribing to Firmware events appEui64 " + bootId + " purifierIp " + purifierIp);
		String portUrl = Utils.getPortUrl(Port.FIRMWARE, purifierIp);
		subscribe(bootId, portUrl, true);
	}
	
	public void unSubscribeFromFirmwareEvents(String bootId, String purifierIp) {
		ALog.d(ALog.SUBSCRIPTION, "Unsubscribing from Firmware events appEui64: " + bootId);
		String portUrl = Utils.getPortUrl(Port.FIRMWARE, purifierIp);
		unSubscribe(bootId, portUrl, true);
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
	
	private void subscribe(String bootId, String url, boolean isLocal) {
		String subscriberId = getSubscriberId(bootId, isLocal);
		ALog.d(ALog.SUBSCRIPTION, "SubscriptionManager$subscribe bootId " + bootId + " URL " + url + " isLocal " + isLocal);
		if(isLocal) {
			TaskPutDeviceDetails subscribe = new TaskPutDeviceDetails(JSONBuilder.getDICommBuilderForSubscribe(subscriberId, LOCAL_SUBSCRIPTIONTIME), url, this,AppConstants.REQUEST_METHOD_POST) ;
			Thread subscibeThread = new Thread(subscribe) ;
			subscibeThread.start();
		}
		else {
			CPPController.getInstance(PurAirApplication.getAppContext()).
			publishEvent(JSONBuilder.getPublishEventBuilderForSubscribe(AppConstants.EVENTSUBSCRIBER_KEY, subscriberId), 
					AppConstants.DI_COMM_REQUEST, AppConstants.SUBSCRIBE, subscriberId,"", 20,CPP_SUBSCRIPTIONTIME) ;
		}
	}
	
	private void unSubscribe(String bootId,String url, boolean isLocal) {
		String subscriberId = getSubscriberId(bootId, isLocal);
		if (isLocal) {
			TaskPutDeviceDetails unSubscribe = new TaskPutDeviceDetails(JSONBuilder.getDICommBuilderForSubscribe(subscriberId,LOCAL_SUBSCRIPTIONTIME), url, this,AppConstants.REQUEST_METHOD_DELETE) ;
			Thread unSubscibeThread = new Thread(unSubscribe) ;
			unSubscibeThread.start() ;
		}
		else {
			CPPController.getInstance(PurAirApplication.getAppContext()).
			publishEvent(JSONBuilder.getPublishEventBuilderForSubscribe(AppConstants.EVENTSUBSCRIBER_KEY, subscriberId),
					AppConstants.DI_COMM_REQUEST, AppConstants.UNSUBSCRIBE, subscriberId,"", 20, CPP_SUBSCRIPTIONTIME) ;
		}
	}
	
	private String getSubscriberId(String bootId, boolean isLocal) {
		String appEui64 = SessionDto.getInstance().getEui64();
		if (appEui64 != null) return appEui64;
		if (isLocal) return bootId; // Fallback for local subscription when no cpp connection
		return null;
	}
	
	@Override
	public void onUDPEventReceived(String data) {
		String decryptedData = new DISecurity(null).decryptData(data, Utils.getPurifierId()) ;
		if (decryptedData == null ) return;
		
		ALog.i(ALog.SUBSCRIPTION, decryptedData) ;
		if (subscriptionEventListener != null) {
			subscriptionEventListener.onSubscribeEventOccurred(decryptedData) ;
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		//TODO if response code not 200? retry?
		if(responseCode != HttpURLConnection.HTTP_OK ) {
			ALog.i(ALog.SUBSCRIPTION, "Subscription failed");
			return;
		}

		ALog.i(ALog.SUBSCRIPTION, "Subscription successfull");
		onUDPEventReceived(responseData); // Response already contains first subscription events, treat as UDP
 	}
} 
