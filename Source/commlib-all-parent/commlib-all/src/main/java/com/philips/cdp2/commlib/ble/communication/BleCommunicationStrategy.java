/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.request.BleGetRequest;
import com.philips.cdp2.commlib.ble.request.BlePutRequest;
import com.philips.cdp2.commlib.ble.request.BleRequest;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.pins.shinelib.SHNDevice;

import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The type BleCommunicationStrategy.
 */
public class BleCommunicationStrategy extends CommunicationStrategy {

    @NonNull
    private final String mCppId;
    @NonNull
    private final BleDeviceCache mDeviceCache;
    @NonNull
    private final ScheduledThreadPoolExecutor mExecutor;

    private AtomicBoolean disconnectAfterRequest = new AtomicBoolean(true);

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
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
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
    public void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        if (isAvailable()) {
            SHNDevice device = mDeviceCache.getDeviceMap().get(mCppId);
            device.connect();
        }
        disconnectAfterRequest.set(false);
    }

    /**
     * Disables continuous connection to the appliance, after each request the connection will
     * be severed to preserve battery life.
     *
     * Note that
     */
    @Override
    public void disableCommunication() {
        if (isAvailable() && mExecutor.getQueue().isEmpty() && mExecutor.getActiveCount() == 0) {
            SHNDevice device = mDeviceCache.getDeviceMap().get(mCppId);
            device.disconnect();
        }
        disconnectAfterRequest.set(true);
    }

    @VisibleForTesting
    protected void dispatchRequest(final BleRequest request) {
        mExecutor.execute(request);
    }
}
