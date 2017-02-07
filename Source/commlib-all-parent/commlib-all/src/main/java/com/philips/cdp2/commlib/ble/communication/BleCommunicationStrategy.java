/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
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
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.philips.cdp.dicommclient.util.GsonProvider.EMPTY_JSON_OBJECT_STRING;
import static java.lang.System.currentTimeMillis;

/**
 * The type BleCommunicationStrategy.
 */
public class BleCommunicationStrategy extends CommunicationStrategy {

    @NonNull
    private final String cppId;
    @NonNull
    private final BleDeviceCache deviceCache;
    private final int subscriptionPollingInterval;
    private final ScheduledThreadPoolExecutor requestExecutor;

    private AtomicBoolean disconnectAfterRequest = new AtomicBoolean(true);
    private Map<PortParameters, Subscription> subscriptionsCache = new ConcurrentHashMap<>();

    private class PortParameters {
        String portName;
        int productId;

        PortParameters(@NonNull String portName, int productId) {
            this.portName = portName;
            this.productId = productId;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;

            PortParameters that = (PortParameters) other;

            if (productId != that.productId) return false;
            return portName.equals(that.portName);
        }

        @Override
        public int hashCode() {
            int result = portName.hashCode();
            result = 31 * result + productId;
            return result;
        }
    }

    private class Subscription implements Runnable {

        private static final int POLLING_INTERVAL = 2000;

        @NonNull
        private final PortParameters portParameters;
        @NonNull
        private final ResponseHandler responseHandler;
        private final long endTime;
        @NonNull
        private ScheduledFuture<?> future;

        Subscription(@NonNull ScheduledExecutorService executor, @NonNull PortParameters portParameters, final int timeToLiveMillis, final @NonNull ResponseHandler responseHandler) {
            this.portParameters = portParameters;
            this.endTime = currentTimeMillis() + timeToLiveMillis;
            this.responseHandler = responseHandler;

            this.future = executor.scheduleWithFixedDelay(this, 0, POLLING_INTERVAL, TimeUnit.MILLISECONDS);
        }

        @Override
        public void run() {
            if (currentTimeMillis() > endTime) {
                cancel();
            } else {
                getProperties(portParameters.portName, portParameters.productId, responseHandler);
            }
        }

        public void cancel() {
            future.cancel(false);
        }
    }

    /**
     * Instantiates a new Ble communication strategy with a sensible default subscription polling interval value.
     *
     * @param cppId       the cpp id
     * @param deviceCache the device cache
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache) {
        this(cppId, deviceCache, 2000);
    }

    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache, final int subscriptionPollingInterval) {
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
            Subscription subscription = new Subscription(this.requestExecutor, portParameters, subscriptionTtl * 1000, responseHandler);
            this.subscriptionsCache.put(portParameters, subscription);

            responseHandler.onSuccess(EMPTY_JSON_OBJECT_STRING);
        }
    }

    @Override
    public synchronized void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        PortParameters portParameters = new PortParameters(portName, productId);
        Subscription subscription = this.subscriptionsCache.get(portParameters);

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
