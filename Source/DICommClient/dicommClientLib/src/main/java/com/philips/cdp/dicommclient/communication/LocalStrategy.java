/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import java.util.Map;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ExchangeKeyRequest;
import com.philips.cdp.dicommclient.request.LocalRequest;
import com.philips.cdp.dicommclient.request.LocalRequestType;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.security.DISecurity.EncryptionDecryptionFailedListener;
import com.philips.cdp.dicommclient.subscription.LocalSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.subscription.UdpEventReceiver;

public class LocalStrategy extends CommunicationStrategy {
    private final RequestQueue mRequestQueue;
    private DISecurity mDISecurity;
    private boolean isKeyExchangeOngoing;
    private LocalSubscriptionHandler mLocalSubscriptionHandler;
    private final NetworkNode networkNode;

    public LocalStrategy(DISecurity diSecurity, final NetworkNode networkNode) {
        mDISecurity = diSecurity;
        this.networkNode = networkNode;
        mDISecurity.setEncryptionDecryptionFailedListener(mEncryptionDecryptionFailedListener);
        mRequestQueue = new RequestQueue();
        mLocalSubscriptionHandler = new LocalSubscriptionHandler(mDISecurity, UdpEventReceiver.getInstance());
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), portName, productId, LocalRequestType.GET, null, responseHandler, mDISecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), portName, productId, LocalRequestType.PUT, dataMap, responseHandler, mDISecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), portName, productId, LocalRequestType.POST, dataMap, responseHandler, mDISecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), portName, productId, LocalRequestType.DELETE, null, responseHandler, mDISecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), portName, productId, LocalRequestType.POST, getSubscriptionData(subscriptionTtl), responseHandler, mDISecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(String portName, int productId,
                            ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LocalRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), portName, productId, LocalRequestType.DELETE, getUnsubscriptionData(), responseHandler, mDISecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return networkNode.getConnectionState().equals(ConnectionState.CONNECTED_LOCALLY);
    }

    private void triggerKeyExchange(NetworkNode networkNode) {
        networkNode.setEncryptionKey(null);
        exchangeKeyIfNecessary(networkNode);
    }

    private void exchangeKeyIfNecessary(NetworkNode networkNode) {
        if (networkNode.getEncryptionKey() == null && !isKeyExchangeOngoing) {
            doKeyExchange(networkNode);
        }
    }

    private void doKeyExchange(final NetworkNode networkNode) {
        ExchangeKeyRequest request = new ExchangeKeyRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), new ResponseHandler() {

            @Override
            public void onSuccess(String key) {
                networkNode.setEncryptionKey(key);
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

    private EncryptionDecryptionFailedListener mEncryptionDecryptionFailedListener = new EncryptionDecryptionFailedListener() {

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
    public void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        mLocalSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        mLocalSubscriptionHandler.disableSubscription();
    }
}
