/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.LocalRequestType;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.ble.request.BleRequest;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * The type BleCommunicationStrategy.
 */
public class BleCommunicationStrategy extends CommunicationStrategy {

    /**
     * The constant DICOMM_MESSAGE_TIMEOUT_MS.
     * <p>
     * This defines the default duration for a request timeout as 5000 ms.
     */
    public static final long DICOMM_MESSAGE_TIMEOUT_MS = 5000L;

    @NonNull
    private final String mCppId;
    @NonNull
    private final BleDeviceCache mDeviceCache;
    @NonNull
    private final ScheduledThreadPoolExecutor mExecutor;

    /**
     * Instantiates a new BleCommunicationStrategy.
     *
     * @param cppId       the cpp id
     * @param deviceCache the device cache
     */
    public BleCommunicationStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache) {
        mCppId = cppId;
        mDeviceCache = deviceCache;
        mExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Override
    public void getProperties(final String portName, final int productId, final ResponseHandler responseHandler) {
        dispatchRequest(LocalRequestType.GET, portName, productId, responseHandler);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        dispatchRequest(LocalRequestType.PUT, dataMap, portName, productId, responseHandler);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public boolean isAvailable() {
        return mDeviceCache.getDeviceMap().containsKey(mCppId);
    }

    @Override
    public void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        if (isAvailable()) {
            SHNDevice device = mDeviceCache.getDeviceMap().get(mCppId);
            device.connect();
        }
    }

    @Override
    public void disableCommunication() {
        if (isAvailable()) {
            SHNDevice device = mDeviceCache.getDeviceMap().get(mCppId);
            device.disconnect();
        }
    }

    private void dispatchRequest(LocalRequestType requestType, final String portName, final int productId, ResponseHandler responseHandler) {
        dispatchRequest(requestType, null, portName, productId, responseHandler);
    }

    private void dispatchRequest(LocalRequestType requestType, Map<String, Object> dataMap, final String portName, final int productId, ResponseHandler responseHandler) {
        final BleRequest request = new BleRequest(mDeviceCache, mCppId, portName, productId, requestType, dataMap, responseHandler);
        addTimeoutToRequest(request);
        mExecutor.execute(request);
    }

    /**
     * Add timeout to request.
     * <p>
     * When set, {@link BleRequest#cancel(String)} will be invoked as soon as the number of
     * milliseconds have passed as defined in {@link BleCommunicationStrategy#DICOMM_MESSAGE_TIMEOUT_MS}.
     *
     * @param request the request
     * @return the timer
     */
    protected Timer addTimeoutToRequest(final BleRequest request) {
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                request.cancel("Timeout occurred.");
            }
        }, DICOMM_MESSAGE_TIMEOUT_MS);

        return t;
    }
}
