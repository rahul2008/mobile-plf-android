/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import java.util.Map;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RemoteRequestType;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.request.StartDcsRequest;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

public class RemoteStrategy extends CommunicationStrategy {

    private final RemoteSubscriptionHandler mRemoteSubscriptionHandler;
    private final NetworkNode mNetworkNode;
    private final CppController mCppController;

    private RequestQueue mRequestQueue;
    private boolean isDSCRequestOnGoing;

    public RemoteStrategy(final NetworkNode networkNode, final CppController cppController) {
        mNetworkNode = networkNode;
        mCppController = cppController;

        mRequestQueue = createRequestQueue();
        mRemoteSubscriptionHandler = createRemoteSubscriptionHandler(cppController);
    }

    @Override
    public void getProperties(final String portName, final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(mNetworkNode.getCppId(), portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void putProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(mNetworkNode.getCppId(), portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void addProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(mNetworkNode.getCppId(), portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(mNetworkNode.getCppId(), portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void subscribe(final String portName, final int productId, final int subscriptionTtl,
                          final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(mNetworkNode.getCppId(), portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(final String portName, final int productId,
                            final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(mNetworkNode.getCppId(), portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler, mCppController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return mNetworkNode.getConnectionState().equals(ConnectionState.CONNECTED_REMOTELY);
    }

    @Override
    public void enableSubscription(final SubscriptionEventListener subscriptionEventListener) {
        startDcsIfNecessary();

        mRemoteSubscriptionHandler.enableSubscription(mNetworkNode, subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        mRemoteSubscriptionHandler.disableSubscription();
        mCppController.stopDCSService();
    }

    protected RequestQueue createRequestQueue() {
        return new RequestQueue();
    }

    protected RemoteSubscriptionHandler createRemoteSubscriptionHandler(CppController cppController) {
        return new RemoteSubscriptionHandler(cppController);
    }

    protected StartDcsRequest createStartDcsRequest(ResponseHandler responseHandler) {
        return new StartDcsRequest(mCppController, responseHandler);
    }

    private ResponseHandler responseHandler = new ResponseHandler() {
        @Override
        public void onSuccess(String data) {
            isDSCRequestOnGoing = false;
        }

        @Override
        public void onError(Error error, String errorData) {
            isDSCRequestOnGoing = false;
        }
    };

    private void startDcsIfNecessary() {
        if (mCppController.getState() != CppController.ICP_CLIENT_DCS_STATE.STARTED && !isDSCRequestOnGoing) {
            StartDcsRequest startRequest = createStartDcsRequest(responseHandler);
            isDSCRequestOnGoing = true;
            mRequestQueue.addRequestInFrontOfQueue(startRequest);
        }
    }
}
