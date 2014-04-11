package com.philips.cl.di.dev.pa.purifier;

import java.net.HttpURLConnection;

import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dev.pa.util.JSONBuilder;
import com.philips.cl.di.dev.pa.util.ServerResponseListener;

public class SubscriptionManager implements UDPEventListener, ServerResponseListener {
	
	private static final int TTL = 3360 ;
	
	private static SubscriptionManager subscriptionManager ;
	
	private SubscriptionEventListener subscriptionEventListener ;
	
	private UDPSocketManager udpManagerThread ;
		
	private SubscriptionManager() {
		udpManagerThread = new UDPSocketManager() ;
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
 	
	public void subscribe(String cppID, String url) {
		ALog.i(ALog.SUBSCRIPTION, "subscribe") ;
		TaskPutDeviceDetails subscribe = new TaskPutDeviceDetails(JSONBuilder.getDICommBuilderForSubscribe(cppID,TTL), url, this,AppConstants.REQUEST_METHOD_POST) ;
		Thread subscibeThread = new Thread(subscribe) ;
		subscibeThread.start() ;
	}
	
	public void unSubscribe(String cppID,String url) {
		ALog.i(ALog.SUBSCRIPTION, "unsubscribe") ;
		TaskPutDeviceDetails unSubscribe = new TaskPutDeviceDetails(JSONBuilder.getDICommBuilderForSubscribe(cppID,TTL), url, this,AppConstants.REQUEST_METHOD_DELETE) ;
		Thread unSubscibeThread = new Thread(unSubscribe) ;
		unSubscibeThread.start() ;
	}
	
	public void openUDPSocket() {
		ALog.i(ALog.SUBSCRIPTION, "startUDPListener") ;
		if( udpManagerThread == null ) {
			udpManagerThread = new UDPSocketManager() ;
		}
		udpManagerThread.setUDPEventListener(this) ;
		if( udpManagerThread != null && !udpManagerThread.isAlive() ) {
			udpManagerThread.start() ;
		}
	}
	
	public void closeUDPSocket() {
		ALog.i(ALog.SUBSCRIPTION, "stopUDPListener") ;
		if(udpManagerThread != null && udpManagerThread.isAlive() ) {
			udpManagerThread.stopUDPListener() ;
			udpManagerThread = null ;
		}
	}
	
	@Override
	public void onUDPEventReceived(String data) {
		String decryptedData = new DISecurity(null).decryptData(data, AppConstants.deviceId) ;
		if( decryptedData != null ) {
			ALog.i(ALog.SUBSCRIPTION, decryptedData) ;
			subscriptionEventListener.onSubscribeEventOccurred(decryptedData) ;
		}
	}

	@Override
	public void receiveServerResponse(int responseCode, String responseData) {
		//if response code not 200? retry?
		if( responseCode != HttpURLConnection.HTTP_OK ) {
			ALog.i(ALog.SUBSCRIPTION, "Subscription failed") ;
		}
 	}
} 
