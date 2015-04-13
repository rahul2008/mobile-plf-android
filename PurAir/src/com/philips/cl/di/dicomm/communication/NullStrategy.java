package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public class NullStrategy extends CommunicationStrategy {

	@Override
	public void getProperties(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED);
	}

	@Override
	public void putProperties(HashMap<String, String> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED);
	}

	@Override
	public void addProperties(HashMap<String, String> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED);
	}

	@Override
	public void subscribe(String portName, int productId, int subscriptionTtl,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED);
	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		return true;
	}

}
