/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.cloud.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.RemoteRequest;
import com.philips.cdp.dicommclient.request.RemoteRequestType;
import com.philips.cdp.dicommclient.request.RequestQueue;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.request.StartDcsRequest;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp2.commlib.core.communication.ObservableCommunicationStrategy;
import com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import static com.philips.cdp.cloudcontroller.api.CloudController.ICPClientDCSState.STARTED;

/**
 * @publicApi
 */
public class CloudCommunicationStrategy extends ObservableCommunicationStrategy {

    private final RemoteSubscriptionHandler remoteSubscriptionHandler;
    private final NetworkNode networkNode;
    private final CloudController cloudController;
    @NonNull
    private final ConnectivityMonitor connectivityMonitor;
    private final RequestQueue requestQueue;

    private boolean isDSCRequestOnGoing;

    private final AvailabilityListener<ConnectivityMonitor> availabilityListener = new AvailabilityListener<ConnectivityMonitor>() {
        @Override
        public void onAvailabilityChanged(@NonNull ConnectivityMonitor connectivityMonitor) {
            notifyAvailabilityChanged();
        }
    };

    private final PropertyChangeListener networkNodePropertyChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals(NetworkNodeDatabaseHelper.KEY_IS_PAIRED)) {
                notifyAvailabilityChanged();
            }
        }
    };

    public CloudCommunicationStrategy(final @NonNull NetworkNode networkNode, final @NonNull CloudController cloudController, final @NonNull ConnectivityMonitor connectivityMonitor) {
        this.networkNode = networkNode;
        this.cloudController = cloudController;
        this.connectivityMonitor = connectivityMonitor;
        this.connectivityMonitor.addAvailabilityListener(availabilityListener);

        requestQueue = createRequestQueue();
        remoteSubscriptionHandler = createRemoteSubscriptionHandler(cloudController);

        networkNode.addPropertyChangeListener(networkNodePropertyChangeListener);
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
    public void subscribe(final String portName, final int productId, final int subscriptionTtl, final ResponseHandler responseHandler) {
        startDcsIfNecessary();
        remoteSubscriptionHandler.enableSubscription(networkNode, subscriptionEventListeners);

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.SUBSCRIBE, getSubscriptionData(subscriptionTtl), responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public void unsubscribe(final String portName, final int productId, final ResponseHandler responseHandler) {
        startDcsIfNecessary();
        remoteSubscriptionHandler.disableSubscription();
        cloudController.stopDCSService();

        RemoteRequest request = new RemoteRequest(networkNode.getCppId(), portName, productId, RemoteRequestType.UNSUBSCRIBE, getUnsubscriptionData(), responseHandler, cloudController);
        requestQueue.addRequest(request);
    }

    @Override
    public boolean isAvailable() {
        return networkNode.getPairedState() == NetworkNode.PairingState.PAIRED && connectivityMonitor.isAvailable();
    }

    @Override
    public void enableCommunication() {
        // NOP
    }

    @Override
    public void disableCommunication() {
        // NOP
    }

    @VisibleForTesting
    RequestQueue createRequestQueue() {
        return new RequestQueue();
    }

    @VisibleForTesting
    RemoteSubscriptionHandler createRemoteSubscriptionHandler(CloudController cloudController) {
        return new RemoteSubscriptionHandler(cloudController);
    }

    @VisibleForTesting
    StartDcsRequest createStartDcsRequest(ResponseHandler responseHandler) {
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
        if (cloudController.getState() == STARTED || isDSCRequestOnGoing) {
            return;
        }

        isDSCRequestOnGoing = true;
        requestQueue.addRequestInFrontOfQueue(createStartDcsRequest(responseHandler));
    }
}
