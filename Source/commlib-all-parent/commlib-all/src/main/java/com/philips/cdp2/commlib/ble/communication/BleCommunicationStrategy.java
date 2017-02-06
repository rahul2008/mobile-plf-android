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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type BleCommunicationStrategy.
 */
public class BleCommunicationStrategy extends CommunicationStrategy {

    @NonNull
    private final String mCppId;
    @NonNull
    private final BleDeviceCache mDeviceCache;
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

    private class Subscription {

        private static final int POLLING_INTERVAL = 2000;

        @NonNull
        private final String portName;
        private final int productId;
        private final int timeToLive;
        private final ResponseHandler responseHandler;
        private ScheduledExecutorService executor;

        Subscription(@NonNull String portName, final int productId, final int timeToLive, final ResponseHandler responseHandler) {
            this.portName = portName;
            this.productId = productId;
            this.timeToLive = timeToLive;
            this.responseHandler = responseHandler;
        }

        void start() {
            this.executor = Executors.newSingleThreadScheduledExecutor();
            this.executor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    getProperties(portName, productId, responseHandler);
                }
            }, 0, POLLING_INTERVAL, TimeUnit.MILLISECONDS);

            // Stop polling after TTL elapsed
            Executors.newSingleThreadScheduledExecutor().schedule(new Runnable() {
                @Override
                public void run() {
                    cancel();
                }
            }, timeToLive, TimeUnit.SECONDS);
        }

        void cancel() {
            if (this.executor == null) {
                return;
            }
            this.executor.shutdown();
        }
    }

    /**
     * Instantiates a new BleCommunicationStrategy.
     *
     * @param cppId       the cpp id
     * @param deviceCache the device cache
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache) {
        mCppId = cppId;
        mDeviceCache = deviceCache;

        requestExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void getProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        final BleRequest request = new BleGetRequest(mDeviceCache, mCppId, portName, productId, responseHandler, disconnectAfterRequest);
        dispatchRequest(request);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        final BleRequest request = new BlePutRequest(mDeviceCache, mCppId, portName, productId, dataMap, responseHandler, disconnectAfterRequest);
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
        Subscription subscription = new Subscription(portName, productId, subscriptionTtl, responseHandler);

        if (this.subscriptionsCache.put(portParameters, subscription) == null) {
            subscription.start();

            responseHandler.onSuccess(null);
        } else {
            responseHandler.onError(Error.PROPERTY_ALREADY_EXISTS, null);
        }
    }

    @Override
    public synchronized void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        PortParameters portParameters = new PortParameters(portName, productId);
        Subscription subscription = this.subscriptionsCache.get(portParameters);

        if (subscription == null) {
            responseHandler.onError(Error.NOT_SUBSCRIBED, null);
        } else {
            subscription.cancel();
            this.subscriptionsCache.remove(subscription);

            responseHandler.onSuccess(null);
        }
    }

    @Override
    public boolean isAvailable() {
        return mDeviceCache.getDeviceMap().containsKey(mCppId);
    }

    /**
     * Enables continuous connection to the appliance, allowing for faster data transfer.
     *
     * @param subscriptionEventListener
     */
    @Override
    public synchronized void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        if (isAvailable()) {
            SHNDevice device = mDeviceCache.getDeviceMap().get(mCppId);
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
            SHNDevice device = mDeviceCache.getDeviceMap().get(mCppId);
            device.disconnect();
        }
        disconnectAfterRequest.set(true);
    }

    @VisibleForTesting
    protected void dispatchRequest(final BleRequest request) {
        requestExecutor.execute(request);
    }
}
