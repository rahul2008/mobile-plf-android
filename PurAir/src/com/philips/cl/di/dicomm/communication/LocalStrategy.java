package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;
import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.LocalRequest;
import com.philips.cl.di.dev.pa.security.DISecurity;

public class LocalStrategy extends CommunicationStrategy {
	private final RequestQueue mRequestQueue;

	public LocalStrategy(){
		mRequestQueue = new RequestQueue();
	}

	@Override
	public void getProperties(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		// TODO DICOMM Refactor, check disecurity and datamap
		LocalRequest request = new LocalRequest(networkNode, portName, productId, LocalRequestType.GET, null, responseHandler, new DISecurity(null));
		mRequestQueue.addRequest(request);
	}

	@Override
	public void putProperties(Map<String, Object> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		// TODO DICOMM Refactor, check disecurity
		LocalRequest request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.PUT, dataMap, responseHandler, new DISecurity(null));
		mRequestQueue.addRequest(request);
	}

	@Override
	public void addProperties(Map<String,Object> dataMap,String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		// TODO DICOMM Refactor, check disecurity
		LocalRequest request = new LocalRequest(networkNode, portName, productId, LocalRequestType.PUT, dataMap, responseHandler, new DISecurity(null));
		mRequestQueue.addRequest(request);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		// TODO DICOMM Refactor, check disecurity, make sure local/remote requests support array ports, use arrayPortId
		LocalRequest request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.DELETE, null, responseHandler, new DISecurity(null));
		mRequestQueue.addRequest(request);
	}

	@Override
	public void subscribe(String portName,int productId, int subscriptionTtl, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		// TODO DICOMM Refactor, check disecurity
		LocalRequest request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.POST, getSubscriptionData(subscriptionTtl), responseHandler, new DISecurity(null));
		mRequestQueue.addRequest(request);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		// TODO DICOMM Refactor, check disecurity, requesttype
		LocalRequest request = new LocalRequest(networkNode, portName, productId, LocalRequestType.DELETE, null, responseHandler, new DISecurity(null));
		mRequestQueue.addRequest(request);

	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		if(networkNode.getConnectionState().equals(ConnectionState.CONNECTED_LOCALLY)){
			return true;
		}
		return false;
	}

}
