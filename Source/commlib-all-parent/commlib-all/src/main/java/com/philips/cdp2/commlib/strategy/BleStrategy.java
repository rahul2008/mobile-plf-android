/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.strategy;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.request.LocalRequestType;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.request.BleRequest;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class BleStrategy extends CommunicationStrategy {

    public static final long DICOMM_MESSAGE_TIMEOUT_MS = 5000L;

    @NonNull
    private final String mCppId;
    @NonNull
    private final BleDeviceCache mDeviceCache;
    @NonNull
    private final ScheduledThreadPoolExecutor mExecutor;

    public BleStrategy(@NonNull String cppId, @NonNull BleDeviceCache deviceCache) {
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
    }

    @Override
    public void disableCommunication() {
    }

    private void dispatchRequest(LocalRequestType requestType, final String portName, final int productId, ResponseHandler responseHandler) {
        final BleRequest request = new BleRequest(mDeviceCache.getDeviceMap().get(mCppId), portName, productId, requestType, null, responseHandler);
        addTimeoutToRequest(request);
        mExecutor.execute(request);
    }

    private void dispatchRequest(LocalRequestType requestType, Map<String, Object> dataMap, final String portName, final int productId, ResponseHandler responseHandler) {
        final BleRequest request = new BleRequest(mDeviceCache.getDeviceMap().get(mCppId), portName, productId, requestType, dataMap, responseHandler);
        addTimeoutToRequest(request);
        mExecutor.execute(request);
    }


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
