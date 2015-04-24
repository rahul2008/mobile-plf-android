package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public abstract class CommunicationStrategy {
	
	public static final String SUBSCRIBER_KEY = "subscriber";
	public static final String TTL_KEY = "ttl";
	public static final int SUBSCRIPTION_TTL = 300;
	
	public abstract void getProperties(String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void putProperties(HashMap<String,String> dataMap, String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void addProperties(HashMap<String,String> dataMap, String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void deleteProperties(String portName, int productId, int arrayPortId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void subscribe(String portName,int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void unsubscribe(String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract boolean isAvailable(NetworkNode networkNode);
	
	// TODO: DICOMM Refactor, remove dependency on cppcontroller and purair app context
	protected HashMap<String, String> getSubscriptionData() {
		HashMap<String,String> dataMap = new HashMap<String, String>();
		dataMap.put(SUBSCRIBER_KEY, CPPController.getInstance(PurAirApplication.getAppContext()).getAppCppId());
		dataMap.put(TTL_KEY, String.valueOf(SUBSCRIPTION_TTL));
		return dataMap;
	}
}
