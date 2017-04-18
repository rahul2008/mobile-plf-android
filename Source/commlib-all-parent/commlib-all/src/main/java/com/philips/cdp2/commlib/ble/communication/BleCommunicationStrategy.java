/*
 * Copyright (c) Koninklijke Philips N.V. 2016, 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.communication.PollingSubscription.Callback;
import com.philips.cdp2.commlib.ble.request.BleGetRequest;
import com.philips.cdp2.commlib.ble.request.BlePutRequest;
import com.philips.cdp2.commlib.ble.request.BleRequest;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import com.philips.cdp2.commlib.core.util.VerboseRunnable;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.cdp.dicommclient.util.GsonProvider.EMPTY_JSON_OBJECT_STRING;

/**
 * The type BleCommunicationStrategy.
 */
public class BleCommunicationStrategy extends CommunicationStrategy {

    private static final long DEFAULT_SUBSCRIPTION_POLLING_INTERVAL = 2000;

    @NonNull
    private final String cppId;
    @NonNull
    private final BleDeviceCache deviceCache;
    @NonNull
    private final ScheduledThreadPoolExecutor requestExecutor;
    @NonNull
    private AtomicBoolean disconnectAfterRequest = new AtomicBoolean(true);
    @NonNull
    private Handler callbackHandler;

    private final long subscriptionPollingInterval;
    private Map<PortParameters, PollingSubscription> subscriptionsCache = new ConcurrentHashMap<>();

    private boolean isConnected;

    /**
     * Instantiates a new Ble communication strategy with a sensible default subscription polling interval value.
     *
     * @param cppId       the cpp id
     * @param deviceCache the device cache
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache) {
        this(cppId, deviceCache, HandlerProvider.createHandler());
    }

    /**
     * Instantiates a new BleCommunicationStrategy that provides callbacks on the specified handler.
     *
     * @param cppId           the cpp id
     * @param deviceCache     the device cache
     * @param callbackHandler the handler on which callbacks will be posted
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache, @NonNull Handler callbackHandler) {
        this(cppId, deviceCache, callbackHandler, DEFAULT_SUBSCRIPTION_POLLING_INTERVAL);
    }

    /**
     * Instantiates a new BleCommunicationStrategy that provides callbacks on the specified handler.
     *
     * @param cppId                       the cpp id
     * @param deviceCache                 the device cache
     * @param callbackHandler             the handler on which callbacks will be posted
     * @param subscriptionPollingInterval the interval used for polling subscriptions
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache, @NonNull Handler callbackHandler, long subscriptionPollingInterval) {
        this.cppId = cppId;
        this.deviceCache = deviceCache;
        this.callbackHandler = callbackHandler;
        this.subscriptionPollingInterval = subscriptionPollingInterval;
        this.requestExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void getProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        final BleRequest request = new BleGetRequest(deviceCache, cppId, portName, productId, responseHandler, callbackHandler, disconnectAfterRequest);
        dispatchRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        final BleRequest request = new BlePutRequest(deviceCache, cppId, portName, productId, dataMap, responseHandler, callbackHandler, disconnectAfterRequest);
        dispatchRequest(request);
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

        if (!this.subscriptionsCache.containsKey(portParameters)) {
            final PollingSubscription subscription = new PollingSubscription(this, portParameters, subscriptionPollingInterval, subscriptionTtl * 1000, new ResponseHandler() {
                @Override
                public void onSuccess(String data) {
                    notifySubscriptionEventListeners(data);
                }

                @Override
                public void onError(Error error, String errorData) {
                    DICommLog.e(DICommLog.LOCAL_SUBSCRIPTION, String.format(Locale.US, "Subscription - onError, error [%s], message [%s]", error, errorData));
                }
            });

            subscription.addCancelCallback(new Callback() {
                @Override
                public void onCancel() {
                    subscriptionsCache.remove(portParameters);
                }
            });
            this.subscriptionsCache.put(portParameters, subscription);
        }
        responseHandler.onSuccess(EMPTY_JSON_OBJECT_STRING);
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
        return deviceCache.contains(cppId);
    }

    /**
     * Enables continuous connection to the appliance, allowing for faster data transfer.
     *
     * @param subscriptionEventListener
     */
    @Override
    public void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        if (isAvailable()) {
            if (!isConnected) {
                SHNDevice device = deviceCache.findByCppId(cppId).getDevice();
                device.connect();
                isConnected = true;
            }
        }
        disconnectAfterRequest.set(false);
    }

    /**
     * Disables continuous connection to the appliance, after each request the connection will
     * be severed to preserve battery life.
     */
    @Override
    public void disableCommunication() {
        if (isAvailable() && requestExecutor.getQueue().isEmpty() && requestExecutor.getActiveCount() == 0) {
            if (isConnected) {
                SHNDevice device = deviceCache.findByCppId(cppId).getDevice();
                device.disconnect();
                isConnected = false;
            }
        }
        disconnectAfterRequest.set(true);
    }

    @VisibleForTesting
    protected void dispatchRequest(final BleRequest request) {
        requestExecutor.execute(new VerboseRunnable(request));
    }
}
