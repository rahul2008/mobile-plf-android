package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public class CommunicationMarshal extends CommunicationStrategy {

	@Override
	public void getProperties(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		findAvailableStrategy(networkNode).getProperties(portName, productId, responseHandler, networkNode);
	}

	@Override
	public void putProperties(HashMap<String, String> dataMap, String portName,
			int productId, ResponseHandler responseHandler,
			NetworkNode networkNode) {
		findAvailableStrategy(networkNode).putProperties(dataMap, portName, productId, responseHandler, networkNode);
	}

	@Override
	public void addProperties(HashMap<String, String> dataMap, String portName,
			int productId, ResponseHandler responseHandler,
			NetworkNode networkNode) {
		findAvailableStrategy(networkNode).addProperties(dataMap, portName, productId, responseHandler, networkNode);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		findAvailableStrategy(networkNode).deleteProperties(portName, productId, arrayPortId,responseHandler, networkNode);
	}

	@Override
	public void subscribe(HashMap<String, String> dataMap, String portName,
			int productId, ResponseHandler responseHandler,
			NetworkNode networkNode) {
		findAvailableStrategy(networkNode).subscribe(dataMap, portName, productId, responseHandler, networkNode);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		findAvailableStrategy(networkNode).unsubscribe(portName, productId, responseHandler, networkNode);
	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		return true;
	}
	
	private CommunicationStrategy findAvailableStrategy(NetworkNode networkNode){
		// TODO:DICOMM Refactor, requestQueue not passed, check
		LocalStrategy localStrategy = new LocalStrategy(null);
		RemoteStrategy remoteStrategy = new RemoteStrategy();
		if(localStrategy.isAvailable(networkNode)){
			return localStrategy;
		}else if(remoteStrategy.isAvailable(networkNode)){
			return remoteStrategy;
		}
		return new NullStrategy();
	}
}
