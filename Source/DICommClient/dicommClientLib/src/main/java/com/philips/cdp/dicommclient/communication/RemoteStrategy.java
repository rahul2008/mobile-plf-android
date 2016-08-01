/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import java.util.Map;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RemoteRequestType;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

public class RemoteStrategy extends CommunicationStrategy {
    private final RequestQueue mRequestQueue;
    private final RemoteSubscriptionHandler mRemoteSuscriptionHandler;
    private final NetworkNode networkNode;
    private final CppController mCppController;

    public RemoteStrategy(final NetworkNode networkNode, final CppController cppController) {
        this.networkNode = networkNode;
        mRequestQueue = new RequestQueue();
        mCppController = cppController;
        mRemoteSuscriptionHandler = new RemoteSubscriptionHandler(cppController);
    }

    @Override
    public void getProperties(String portName, int productId,
                              ResponseHandler responseHandler) {
        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId,
                              ResponseHandler responseHandler) {
        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName,
                              int productId,
                              ResponseHandler responseHandler) {
        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl,
                          ResponseHandler responseHandler) {
        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(String portName, int productId,
                            ResponseHandler responseHandler) {
        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return networkNode.getConnectionState().equals(ConnectionState.CONNECTED_REMOTELY);
    }

    @Override
    public void enableSubscription(SubscriptionEventListener subscriptionEventListener) {
        mRemoteSuscriptionHandler.enableSubscription(networkNode, subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        mRemoteSuscriptionHandler.disableSubscription();
    }
}
