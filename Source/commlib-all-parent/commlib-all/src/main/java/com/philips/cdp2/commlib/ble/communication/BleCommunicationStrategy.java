/*
 * Copyright (c) Koninklijke Philips N.V. 2016, 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.request.BleGetRequest;
import com.philips.cdp2.commlib.ble.request.BlePutRequest;
import com.philips.cdp2.commlib.ble.request.BleRequest;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.pins.shinelib.SHNDevice;

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
    private final long subscriptionPollingInterval;
    private final ScheduledThreadPoolExecutor requestExecutor;

    private AtomicBoolean disconnectAfterRequest = new AtomicBoolean(true);
    private Map<PortParameters, PollingSubscription> subscriptionsCache = new ConcurrentHashMap<>();

    /**
     * Instantiates a new Ble communication strategy with a sensible default subscription polling interval value.
     *
     * @param cppId       the cpp id
     * @param deviceCache the device cache
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache) {
        this(cppId, deviceCache, DEFAULT_SUBSCRIPTION_POLLING_INTERVAL);
    }

    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache, final long subscriptionPollingInterval) {
        this.cppId = cppId;
        this.deviceCache = deviceCache;
        this.subscriptionPollingInterval = subscriptionPollingInterval;
        this.requestExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void getProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        final BleRequest request = new BleGetRequest(deviceCache, cppId, portName, productId, responseHandler, disconnectAfterRequest);
        dispatchRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        final BleRequest request = new BlePutRequest(deviceCache, cppId, portName, productId, dataMap, responseHandler, disconnectAfterRequest);
        dispatchRequest(request);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public synchronized void subscribe(final String portName, final int productId, final int subscriptionTtl, final ResponseHandler responseHandler) {
        PortParameters portParameters = new PortParameters(portName, productId);

        if (this.subscriptionsCache.containsKey(portParameters)) {
            responseHandler.onError(Error.PROPERTY_ALREADY_EXISTS, EMPTY_JSON_OBJECT_STRING);
        } else {
            PollingSubscription subscription = new PollingSubscription(this, this.requestExecutor, portParameters, subscriptionPollingInterval, subscriptionTtl * 1000, responseHandler);
            this.subscriptionsCache.put(portParameters, subscription);

            responseHandler.onSuccess(EMPTY_JSON_OBJECT_STRING);
        }
    }

    @Override
    public synchronized void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        PortParameters portParameters = new PortParameters(portName, productId);
        PollingSubscription subscription = this.subscriptionsCache.get(portParameters);

        if (subscription == null) {
            responseHandler.onError(Error.NOT_SUBSCRIBED, EMPTY_JSON_OBJECT_STRING);
        } else {
            subscription.cancel();
            this.subscriptionsCache.remove(portParameters);

            responseHandler.onSuccess(EMPTY_JSON_OBJECT_STRING);
        }
    }

    @Override
    public boolean isAvailable() {
        return deviceCache.getDeviceMap().containsKey(cppId);
    }

    /**
     * Enables continuous connection to the appliance, allowing for faster data transfer.
     *
     * @param subscriptionEventListener
     */
    @Override
    public synchronized void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        if (isAvailable()) {
            SHNDevice device = deviceCache.getDeviceMap().get(cppId);
            device.connect();
        }
        disconnectAfterRequest.set(false);
    }

    /**
     * Disables continuous connection to the appliance, after each request the connection will
     * be severed to preserve battery life.
     * <p>
     * Note that
     */
    @Override
    public synchronized void disableCommunication() {
        if (isAvailable() && requestExecutor.getQueue().isEmpty() && requestExecutor.getActiveCount() == 0) {
            SHNDevice device = deviceCache.getDeviceMap().get(cppId);
            device.disconnect();
        }
        disconnectAfterRequest.set(true);
    }

    @VisibleForTesting
    protected void dispatchRequest(final BleRequest request) {
        requestExecutor.execute(request);
    }
}
