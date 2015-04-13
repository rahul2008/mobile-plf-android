package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public class CommunicationMarshal extends CommunicationStrategy {
	
	private LocalStrategy mLocalStrategy = new LocalStrategy();
	private RemoteStrategy mRemoteStrategy = new RemoteStrategy();
	private NullStrategy mNullStrategy = new NullStrategy();

	@Override
	public void getProperties(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		findAvailableStrategy(networkNode).getProperties(portName, productId, networkNode, responseHandler);
	}

	@Override
	public void putProperties(HashMap<String, String> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		findAvailableStrategy(networkNode).putProperties(dataMap, portName, productId, networkNode, responseHandler);
	}

	@Override
	public void addProperties(HashMap<String, String> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		findAvailableStrategy(networkNode).addProperties(dataMap, portName, productId, networkNode, responseHandler);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		findAvailableStrategy(networkNode).deleteProperties(portName, productId, arrayPortId,networkNode, responseHandler);
	}

	@Override
	public void subscribe(String portName, int productId,int subscriptionTtl,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		findAvailableStrategy(networkNode).subscribe(portName, productId,subscriptionTtl, networkNode, responseHandler);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		findAvailableStrategy(networkNode).unsubscribe(portName, productId, networkNode, responseHandler);
	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		return true;
	}
	
	private CommunicationStrategy findAvailableStrategy(NetworkNode networkNode){		
		if(mLocalStrategy.isAvailable(networkNode)){
			return mLocalStrategy;
		}else if(mRemoteStrategy.isAvailable(networkNode)){
			return mRemoteStrategy;
		}
		return mNullStrategy;
	}
}
