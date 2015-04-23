package com.philips.cl.di.dicomm.communication;

import java.util.Map;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.ExchangeKeyRequest;
import com.philips.cl.di.dev.pa.purifier.LocalRequest;
import com.philips.cl.di.dicomm.security.DISecurity;
import com.philips.cl.di.dicomm.security.DISecurity.DecryptionFailedListener;

public class LocalStrategy extends CommunicationStrategy {
	private final RequestQueue mRequestQueue;
    private DISecurity mDISecurity;
    private boolean isKeyExchangeOngoing;

	public LocalStrategy(DISecurity diSecurity){
		mDISecurity = diSecurity;
		mDISecurity.setDecryptionFailedListener(mDecryptionFailedListener);
        mRequestQueue = new RequestQueue();
	}

	@Override
	public void getProperties(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
	    exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode, portName, productId, LocalRequestType.GET, null, responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);
	}

    @Override
	public void putProperties(Map<String, Object> dataMap, String portName,
			int productId, NetworkNode networkNode,
			ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
		Request request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.PUT, dataMap, responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void addProperties(Map<String,Object> dataMap,String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
		Request request = new LocalRequest(networkNode, portName, productId, LocalRequestType.PUT, dataMap, responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void deleteProperties(String portName, int productId, int arrayPortId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
		// TODO DICOMM Refactor, make sure local/remote requests support array ports, use arrayPortId
		Request request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.DELETE, null, responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void subscribe(String portName,int productId, int subscriptionTtl, NetworkNode networkNode,
			ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
		Request request  = new LocalRequest(networkNode, portName, productId, LocalRequestType.POST, getSubscriptionData(subscriptionTtl), responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void unsubscribe(String portName, int productId,
			NetworkNode networkNode, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
		Request request = new LocalRequest(networkNode, portName, productId, LocalRequestType.DELETE, getUnsubscriptionData(), responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);

	}

	@Override
	public boolean isAvailable(NetworkNode networkNode) {
		if(networkNode.getConnectionState().equals(ConnectionState.CONNECTED_LOCALLY)){
			return true;
		}
		return false;
	}

    private void exchangeKeyIfNecessary(NetworkNode networkNode) {
        if(networkNode.getEncryptionKey()==null && !isKeyExchangeOngoing){
            doKeyExchange(networkNode);
        }
    }

    private void doKeyExchange(final NetworkNode networkNode) {
        ExchangeKeyRequest request = new ExchangeKeyRequest(networkNode, new ResponseHandler() {
            
            @Override
            public void onSuccess(String data) {
                isKeyExchangeOngoing = false;
            }
            
            @Override
            public void onError(Error error, String errorData) {
                isKeyExchangeOngoing = false;
            }
        });
        isKeyExchangeOngoing = true;
        mRequestQueue.addRequestInFrontOfQueue(request);
    }
    
    DecryptionFailedListener mDecryptionFailedListener = new DecryptionFailedListener() {

        @Override
        public void onDecryptionFailed(NetworkNode networkNode) {
            networkNode.setEncryptionKey(null);
            exchangeKeyIfNecessary(networkNode);
        }
    };
}
