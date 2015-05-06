package com.philips.cl.di.dicomm.communication;

import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.RemoteRequest;

public class RemoteStrategy extends CommunicationStrategy {
private final RequestQueue mRequestQueue;
private final RemoteSuscriptionHandler mRemoteSuscriptionHandler;

	public RemoteStrategy(){
		mRequestQueue = new RequestQueue();
		mRemoteSuscriptionHandler = new RemoteSuscriptionHandler();
	}

	@Override
	public void getProperties(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void putProperties(Map<String, Object> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void addProperties(Map<String, Object> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void deleteProperties(String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void subscribe(String portName, int productId,int subscriptionTtl,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		RemoteRequest request = new RemoteRequest(networkNode, portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler);
		mRequestQueue.addRequest(request);
	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		if(networkNode.getConnectionState().equals(ConnectionState.CONNECTED_REMOTELY)){
			return true;
		}
		return false;
	}


	@Override
	public void enableSubscription(
			SubscriptionEventListener subscriptionEventListener, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check if there is better way to inject
		mRemoteSuscriptionHandler.registerSubscriptionListener(subscriptionEventListener);
		mRemoteSuscriptionHandler.enableSubscription(networkNode);
	}

	@Override
	public void disableSubscription(
			SubscriptionEventListener subscriptionEventListener, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check if there is better way to inject
		mRemoteSuscriptionHandler.unRegisterSubscriptionListener(subscriptionEventListener);
		mRemoteSuscriptionHandler.disableSubscription();
		
	}

}
