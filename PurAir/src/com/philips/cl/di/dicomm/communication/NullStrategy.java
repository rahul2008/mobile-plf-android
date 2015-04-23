package com.philips.cl.di.dicomm.communication;

import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public class NullStrategy extends CommunicationStrategy {

	@Override
	public void getProperties(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED, null);
	}

	@Override
	public void putProperties(Map<String, Object> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED, null);
	}

	@Override
	public void addProperties(Map<String, Object> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED, null);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED, null);
	}

	@Override
	public void subscribe(String portName, int productId, int subscriptionTtl,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED, null);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
		responseHandler.onError(Error.NOTCONNECTED, null);
	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		return true;
	}

}
