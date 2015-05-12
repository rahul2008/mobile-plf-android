package com.philips.cl.di.dicomm.communication;

import java.util.Map;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.ExchangeKeyRequest;
import com.philips.cl.di.dev.pa.purifier.LocalRequest;
import com.philips.cl.di.dicomm.security.DISecurity;
import com.philips.cl.di.dicomm.security.DISecurity.EncryptionDecryptionFailedListener;

public class LocalStrategy extends CommunicationStrategy {
	private final RequestQueue mRequestQueue;
    private DISecurity mDISecurity;
    private boolean isKeyExchangeOngoing;
    private LocalSubscriptionHandler mLocalSubscriptionHandler;

	public LocalStrategy(DISecurity diSecurity){
		mDISecurity = diSecurity;
		mDISecurity.setEncryptionDecryptionFailedListener(mEncryptionDecryptionFailedListener);
        mRequestQueue = new RequestQueue();
        mLocalSubscriptionHandler = new LocalSubscriptionHandler(mDISecurity);
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
		Request request = new LocalRequest(networkNode, portName, productId, LocalRequestType.POST, dataMap, responseHandler, mDISecurity);
		mRequestQueue.addRequest(request);
	}

	@Override
	public void deleteProperties(String portName, int productId, NetworkNode networkNode, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
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

	private void triggerKeyExchange(NetworkNode networkNode) {
		networkNode.setEncryptionKey(null);
        exchangeKeyIfNecessary(networkNode);
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

    EncryptionDecryptionFailedListener mEncryptionDecryptionFailedListener = new EncryptionDecryptionFailedListener() {

        @Override
        public void onDecryptionFailed(NetworkNode networkNode) {
            triggerKeyExchange(networkNode);
        }

        @Override
        public void onEncryptionFailed(NetworkNode networkNode) {
        	triggerKeyExchange(networkNode);
        }
    };

	@Override
	public void enableSubscription(
			SubscriptionEventListener subscriptionEventListener, NetworkNode networkNode) {
		mLocalSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListener);
	}

	@Override
	public void disableSubscription() {
		mLocalSubscriptionHandler.disableSubscription();
	}
}
