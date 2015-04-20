package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;
import java.util.Map;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public abstract class CommunicationStrategy {

	public static final String SUBSCRIBER_KEY = "subscriber";
	public static final String TTL_KEY = "ttl";

	public abstract void getProperties(String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler);
	public abstract void putProperties(Map<String,Object> dataMap, String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler);
	public abstract void addProperties(Map<String,Object> dataMap, String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler);
	public abstract void deleteProperties(String portName, int productId, int arrayPortId, NetworkNode networkNode, ResponseHandler responseHandler);
	public abstract void subscribe(String portName,int productId, int subscriptionTtl,NetworkNode networkNode, ResponseHandler responseHandler);
	public abstract void unsubscribe(String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler);
	public abstract boolean isAvailable(NetworkNode networkNode);

	protected Map<String, Object> getSubscriptionData(int subscriptionTtl) {
		Map<String,Object> dataMap = getUnsubscriptionData();
		dataMap.put(TTL_KEY, subscriptionTtl);
		return dataMap;
	}

	// TODO: DICOMM Refactor, remove dependency on cppcontroller and purair app context
	protected Map<String, Object> getUnsubscriptionData() {
		Map<String,Object> dataMap = new HashMap<String, Object>();
		dataMap.put(SUBSCRIBER_KEY, CPPController.getInstance(PurAirApplication.getAppContext()).getAppCppId());
		return dataMap;
	}
}
