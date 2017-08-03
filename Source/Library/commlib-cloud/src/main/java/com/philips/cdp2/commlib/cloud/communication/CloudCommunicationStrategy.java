/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.cloud.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

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
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor.ConnectivityListener;

import java.util.Map;

public class CloudCommunicationStrategy extends CommunicationStrategy {

    private final RemoteSubscriptionHandler remoteSubscriptionHandler;
    private final NetworkNode networkNode;
    private final CloudController cloudController;
    @NonNull
    private final ConnectivityMonitor connectivityMonitor;
    private final RequestQueue requestQueue;

    private boolean isDSCRequestOnGoing;
    private boolean isAvailable;

    private final ConnectivityListener connectivityListener = new ConnectivityListener() {
        @Override
        public void onConnectivityChanged(boolean isConnected) {
            isAvailable = isConnected;
        }
    };

    public CloudCommunicationStrategy(final @NonNull NetworkNode networkNode, final @NonNull CloudController cloudController, final @NonNull ConnectivityMonitor connectivityMonitor) {
        this.networkNode = networkNode;
        this.cloudController = cloudController;
        this.connectivityMonitor = connectivityMonitor;
        this.connectivityMonitor.addConnectivityListener(connectivityListener);

        requestQueue = createRequestQueue();
        remoteSubscriptionHandler = createRemoteSubscriptionHandler(cloudController);
    }

    @Override
    public void getProperties(final String portName, final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.GET_PROPS, null, responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public void putProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.PUT_PROPS, dataMap, responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public void addProperties(final Map<String, Object> dataMap, final String portName,
                              final int productId,
                              final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.ADD_PROPS, dataMap, responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public void deleteProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.DEL_PROPS, null, responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public void subscribe(final String portName, final int productId, final int subscriptionTtl,
                          final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(final String portName, final int productId,
                            final ResponseHandler responseHandler) {
        startDcsIfNecessary();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
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

    @NonNull
    @VisibleForTesting
    RequestQueue createRequestQueue() {
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
            requestQueue.addRequestInFrontOfQueue(startRequest);
        }
    }
}
