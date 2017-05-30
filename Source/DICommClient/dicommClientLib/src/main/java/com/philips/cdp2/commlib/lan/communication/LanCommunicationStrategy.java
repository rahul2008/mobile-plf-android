/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */
package com.philips.cdp2.commlib.lan.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ExchangeKeyRequest;
import com.philips.cdp.dicommclient.request.GetKeyRequest;
import com.philips.cdp.dicommclient.request.Request;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.security.DISecurity.EncryptionDecryptionFailedListener;
import com.philips.cdp.dicommclient.subscription.LocalSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.subscription.UdpEventReceiver;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Map;

public class LanCommunicationStrategy extends CommunicationStrategy {
    private final RequestQueue mRequestQueue;
    private DISecurity diSecurity;
    private boolean isKeyExchangeOngoing;
    private LocalSubscriptionHandler mLocalSubscriptionHandler;
    private final NetworkNode networkNode;

    public LanCommunicationStrategy(@NonNull final NetworkNode networkNode) {
        this.networkNode = networkNode;
        this.diSecurity = new DISecurity(networkNode);
        this.diSecurity.setEncryptionDecryptionFailedListener(mEncryptionDecryptionFailedListener);
        mRequestQueue = new RequestQueue();
        mLocalSubscriptionHandler = new LocalSubscriptionHandler(diSecurity, UdpEventReceiver.getInstance());
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), portName, productId, LanRequestType.GET, null, responseHandler, diSecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), portName, productId, LanRequestType.PUT, dataMap, responseHandler, diSecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), portName, productId, LanRequestType.POST, dataMap, responseHandler, diSecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), portName, productId, LanRequestType.DELETE, null, responseHandler, diSecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), portName, productId, LanRequestType.POST, getSubscriptionData(subscriptionTtl), responseHandler, diSecurity);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(String portName, int productId,
                            ResponseHandler responseHandler) {
        exchangeKeyIfNecessary(networkNode);
        Request request = new LanRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), portName, productId, LanRequestType.DELETE, getUnsubscriptionData(), responseHandler, diSecurity);
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
            if (networkNode.getHttps()) {
                getKey(networkNode);
            } else {
                doKeyExchange(networkNode);
            }
        }
    }

    private void doKeyExchange(final NetworkNode networkNode) {
        ExchangeKeyRequest request = new ExchangeKeyRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), new ResponseHandler() {

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

    private void getKey(final NetworkNode networkNode) {
        GetKeyRequest request = new GetKeyRequest(networkNode.getIpAddress(), networkNode.getDICommProtocolVersion(), networkNode.getHttps(), new ResponseHandler() {

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
