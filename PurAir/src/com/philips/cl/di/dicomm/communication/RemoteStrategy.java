package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.RemoteRequest;

public class RemoteStrategy extends CommunicationStrategy {
private final RequestQueue mRequestQueue;
	
	public RemoteStrategy(){
		mRequestQueue = new RequestQueue();
	}

	@Override
	public void getProperties(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void putProperties(HashMap<String, String> dataMap, String portName,
			int productId, ResponseHandler responseHandler,
			NetworkNode networkNode) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void addProperties(HashMap<String, String> dataMap, String portName,
			int productId, ResponseHandler responseHandler,
			NetworkNode networkNode) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		// TODO DICOMM Refactor, make sure to support array ports, use arrayPortId
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void subscribe(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(), responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.UNSUBSCRIBE, null, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		if(networkNode.getConnectionState().equals(ConnectionState.CONNECTED_REMOTELY)){
			return true;
		}
		return false;
	}

}
