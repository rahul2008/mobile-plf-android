package com.philips.cl.di.dicomm.communication;

import java.util.HashMap;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.LocalRequest;
import com.philips.cl.di.dev.pa.security.DISecurity;

public class LocalStrategy extends CommunicationStrategy {
	private final RequestQueue mRequestQueue;
	
	public LocalStrategy(RequestQueue requestQueue){
		mRequestQueue = requestQueue;
	}

	@Override
	public void getProperties(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check disecurity and datamap
		LocalRequest request = new LocalRequest(networkNode, portName, productId, LocalRequestType.GET, null, responseHandler, new DISecurity(null));
		request.execute();
	}

	@Override
	public void putProperties(HashMap<String, String> dataMap, String portName,
			int productId, ResponseHandler responseHandler,
			NetworkNode networkNode) {
		// TODO DICOMM Refactor, check disecurity
		LocalRequest request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.PUT, dataMap, responseHandler, new DISecurity(null));
		request.execute();

	}

	@Override
	public void addProperties(HashMap<String,String> dataMap,String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check disecurity
		LocalRequest request = new LocalRequest(networkNode, portName, productId, LocalRequestType.PUT, dataMap, responseHandler, new DISecurity(null));
		request.execute();

	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check disecurity, make sure to support array ports, use arrayPortId
		LocalRequest request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.DELETE, null, responseHandler, new DISecurity(null));
        request.execute();
	}

	// subscribe needs dataMap, need to check
	@Override
	public void subscribe(HashMap<String,String> dataMap,String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check disecurity, also cross check "post" is used for local subscription 
		LocalRequest request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.POST, dataMap, responseHandler, new DISecurity(null));
		request.execute();
	}

	@Override
	public void unsubscribe(String portName, int productId,
			ResponseHandler responseHandler, NetworkNode networkNode) {
		// TODO DICOMM Refactor, check disecurity, requesttype
		LocalRequest request = new LocalRequest(networkNode, portName, productId, LocalRequestType.DELETE, null, responseHandler, new DISecurity(null));
		request.execute();

	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		if(networkNode.getConnectionState().equals(ConnectionState.CONNECTED_LOCALLY)){
			return true;
		}
		return false;
	}

}
