package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public abstract class CommunicationStrategy {
	
	public abstract void getProperties(String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void putProperties(HashMap<String,String> dataMap, String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void addProperties(HashMap<String,String> dataMap, String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void deleteProperties(String portName, int productId, int arrayPortId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void subscribe(HashMap<String,String> dataMap,String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract void unsubscribe(String portName, int productId, ResponseHandler responseHandler, NetworkNode networkNode);
	public abstract boolean isAvailable(NetworkNode networkNode);
}
