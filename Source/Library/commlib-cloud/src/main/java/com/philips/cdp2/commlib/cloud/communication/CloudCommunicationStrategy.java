/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.cloud.communication;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RemoteRequestType;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.request.StartDcsRequest;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Map;

public class CloudCommunicationStrategy extends CommunicationStrategy {

    private final RemoteSubscriptionHandler remoteSubscriptionHandler;
    private final NetworkNode networkNode;
    private final CloudController cloudController;

    private RequestQueue mRequestQueue;
    private boolean isDSCRequestOnGoing;

    public CloudCommunicationStrategy(final NetworkNode networkNode, final CloudController cloudController) {
        this.networkNode = networkNode;
        if (cloudController == null) {
            throw new IllegalArgumentException("CloudController is null.");
        }
        this.cloudController = cloudController;
        mRequestQueue = createRequestQueue();
        remoteSubscriptionHandler = createRemoteSubscriptionHandler(cloudController);
    }

    @Override
    public void getProperties(final String portName, final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler, cloudController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void putProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler, cloudController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void addProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler, cloudController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler, cloudController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void subscribe(final String portName, final int productId, final int subscriptionTtl,
                          final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler, cloudController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(final String portName, final int productId,
                            final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler, cloudController);
        mRequestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return true; // TODO actually determine connectivity
    }

    @Override
    public void enableCommunication(final SubscriptionEventListener subscriptionEventListener) {
        startDcsIfNecessary();

        remoteSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListener);
    }

    @Override
    public void disableCommunication() {
        remoteSubscriptionHandler.disableSubscription();
        cloudController.stopDCSService();
    }

    protected RequestQueue createRequestQueue() {
        return new RequestQueue();
    }

    protected RemoteSubscriptionHandler createRemoteSubscriptionHandler(CloudController cloudController) {
        return new RemoteSubscriptionHandler(cloudController);
    }

    protected StartDcsRequest createStartDcsRequest(ResponseHandler responseHandler) {
        return new StartDcsRequest(cloudController, responseHandler);
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
        if (cloudController.getState() != CloudController.ICPClientDCSState.STARTED && !isDSCRequestOnGoing) {
            StartDcsRequest startRequest = createStartDcsRequest(responseHandler);
            isDSCRequestOnGoing = true;
            mRequestQueue.addRequestInFrontOfQueue(startRequest);
        }
    }
}
