/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.strategy;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.BleDeviceCache;

import java.util.Map;

public class BleStrategy extends CommunicationStrategy {

    private final String cppId;
    private final BleDeviceCache deviceCache;

    public BleStrategy(String cppId, BleDeviceCache deviceCache) {
        this.cppId = cppId;
        this.deviceCache = deviceCache;
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
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
        return deviceCache.getDeviceMap().containsKey(cppId);
    }

    @Override
    public void enableCommunication(
            SubscriptionEventListener subscriptionEventListener) {
    }

    @Override
    public void disableCommunication() {
    }
}
