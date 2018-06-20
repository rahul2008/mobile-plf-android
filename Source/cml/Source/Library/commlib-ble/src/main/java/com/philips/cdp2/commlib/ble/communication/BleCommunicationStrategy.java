/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.bluelib.plugindefinition.ReferenceNodeDeviceDefinitionInfo;
import com.philips.cdp2.commlib.ble.request.BleGetRequest;
import com.philips.cdp2.commlib.ble.request.BlePutRequest;
import com.philips.cdp2.commlib.ble.request.BleRequest;
import com.philips.cdp2.commlib.core.communication.ObservableCommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import com.philips.cdp2.commlib.core.util.VerboseRunnable;
import com.philips.cdp2.commlib.util.VerboseExecutor;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.cdp2.commlib.core.util.GsonProvider.EMPTY_JSON_OBJECT_STRING;

/**
 * The type BleCommunicationStrategy.
 *
 * @publicApi
 */
public class BleCommunicationStrategy extends ObservableCommunicationStrategy {

    private static final long DEFAULT_SUBSCRIPTION_POLLING_INTERVAL = 2000;

    private static final long CONNECTION_TIMEOUT = 30000L;

    @NonNull
    private final SHNCentral central;

    @NonNull
    private final VerboseExecutor requestExecutor;

    @NonNull
    private final NetworkNode networkNode;

    @NonNull
    @VisibleForTesting
    AtomicBoolean disconnectAfterRequest = new AtomicBoolean(true);

    @NonNull
    private Handler callbackHandler;

    private final long subscriptionPollingInterval;
    private Map<PortParameters, PollingSubscription> subscriptionsCache = new ConcurrentHashMap<>();
    private boolean isAvailable;

    private SHNDevice bleDevice;

    private final SHNCentral.SHNCentralListener centralListener = new SHNCentral.SHNCentralListener() {
        @Override
        public void onStateUpdated(@NonNull SHNCentral shnCentral) {
            handleAvailabilityChange();
        }
    };

    private PropertyChangeListener networkNodePropertyChangeListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            handleAvailabilityChange();
        }
    };

    /**
     * Instantiates a new Ble communication strategy with a sensible default subscription polling interval value.
     */
    public BleCommunicationStrategy(@NonNull final SHNCentral central, @NonNull final NetworkNode networkNode) {
        this(central, networkNode, HandlerProvider.createHandler());
    }

    /**
     * Instantiates a new BleCommunicationStrategy that provides callbacks on the specified handler.
     *
     * @param networkNode     the networkNode
     * @param callbackHandler the handler on which callbacks will be posted
     */
    public BleCommunicationStrategy(@NonNull final SHNCentral central, @NonNull final NetworkNode networkNode, @NonNull final Handler callbackHandler) {
        this(central, networkNode, callbackHandler, DEFAULT_SUBSCRIPTION_POLLING_INTERVAL);
    }

    /**
     * Instantiates a new BleCommunicationStrategy that provides callbacks on the specified handler.
     *
     * @param networkNode                 the networkNode
     * @param callbackHandler             the handler on which callbacks will be posted
     * @param subscriptionPollingInterval the interval used for polling subscriptions
     */
    public BleCommunicationStrategy(@NonNull final SHNCentral central, @NonNull final NetworkNode networkNode, @NonNull final Handler callbackHandler, long subscriptionPollingInterval) {
        this(central, networkNode, callbackHandler, subscriptionPollingInterval, new VerboseExecutor());
    }

    @VisibleForTesting
    BleCommunicationStrategy(@NonNull final SHNCentral central, @NonNull final NetworkNode networkNode, @NonNull final Handler callbackHandler, long subscriptionPollingInterval, @NonNull final VerboseExecutor requestExecutor) {
        this.central = central;
        this.networkNode = networkNode;
        this.requestExecutor = requestExecutor;
        this.subscriptionPollingInterval = subscriptionPollingInterval;
        this.callbackHandler = callbackHandler;

        this.central.registerShnCentralListener(centralListener);
        this.networkNode.addPropertyChangeListener(networkNodePropertyChangeListener);

        isAvailable = isAvailable();
    }

    @Override
    public void getProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        if (isAvailable()) {
            final BleRequest request = new BleGetRequest(getBleDevice(), portName, productId, responseHandler, callbackHandler, disconnectAfterRequest);
            dispatchRequest(request);
        } else {
            responseHandler.onError(Error.CANNOT_CONNECT, "Communication is not available");
        }
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        if (isAvailable()) {
            final BleRequest request = new BlePutRequest(getBleDevice(), portName, productId, dataMap, responseHandler, callbackHandler, disconnectAfterRequest);
            dispatchRequest(request);
        } else {
            responseHandler.onError(Error.CANNOT_CONNECT, "Communication is not available");
        }
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public void subscribe(final String portName, final int productId, final int subscriptionTtl, final ResponseHandler responseHandler) {
        final PortParameters portParameters = new PortParameters(portName, productId);

        final PollingSubscription existingPollingSubscription = subscriptionsCache.remove(portParameters);
        if (existingPollingSubscription != null) {
            existingPollingSubscription.cancel();
        }

        final ResponseHandler pollingResponseHandler = new ResponseHandler() {
            @Override
            public void onSuccess(String data) {
                notifySubscriptionEventListeners(portName, data);
            }

            @Override
            public void onError(Error error, String errorData) {
                DICommLog.e(DICommLog.LOCAL_SUBSCRIPTION, String.format(Locale.US, "Subscription - onError, error [%s], message [%s]", error, errorData));
            }
        };

        final PollingSubscription subscription = createPollingSubscription(subscriptionTtl, portParameters, pollingResponseHandler);
        this.subscriptionsCache.put(portParameters, subscription);

        responseHandler.onSuccess(EMPTY_JSON_OBJECT_STRING);
    }

    @NonNull
    protected PollingSubscription createPollingSubscription(final int subscriptionTtl, final PortParameters portParameters, final ResponseHandler responseHandler) {
        return new PollingSubscription(this, portParameters, subscriptionPollingInterval, subscriptionTtl * 1000, responseHandler);
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        PortParameters portParameters = new PortParameters(portName, productId);
        PollingSubscription subscription = this.subscriptionsCache.get(portParameters);

        if (subscription != null) {
            subscription.cancel();
            this.subscriptionsCache.remove(portParameters);
        }
        responseHandler.onSuccess(EMPTY_JSON_OBJECT_STRING);
    }

    @Override
    public boolean isAvailable() {
        return central.isBluetoothAdapterEnabled() && central.isValidMacAddress(networkNode.getMacAddress());
    }

    /**
     * Enables continuous connection to the appliance, allowing for faster data transfer.
     */
    @Override
    public void enableCommunication() {
        if (isAvailable()) {
            SHNDevice device = getBleDevice();
            device.connect(CONNECTION_TIMEOUT);
        }
        disconnectAfterRequest.set(false);
    }

    /**
     * Disables continuous connection to the appliance, after each request the connection will
     * be severed to preserve battery life.
     */
    @Override
    public void disableCommunication() {
        disconnectAfterRequest.set(true);

        if (isAvailable() && requestExecutor.isIdle()) {
            SHNDevice device = getBleDevice();
            device.disconnect();
        }
    }

    @VisibleForTesting
    protected void dispatchRequest(final BleRequest request) {
        requestExecutor.execute(new VerboseRunnable(request));
    }

    private SHNDevice getBleDevice() {
        if (bleDevice == null) {
            bleDevice = central.createSHNDeviceForAddressAndDefinition(networkNode.getMacAddress(), new ReferenceNodeDeviceDefinitionInfo());
        }
        return bleDevice;
    }

    private void handleAvailabilityChange() {
        boolean newAvailability = isAvailable();

        if (newAvailability != isAvailable) {
            isAvailable = newAvailability;
            notifyAvailabilityChanged();
        }
    }
}
