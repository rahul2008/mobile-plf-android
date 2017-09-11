/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;

import java.util.Map;

public class NullCommunicationStrategy extends ObservableCommunicationStrategy {

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOT_CONNECTED, null);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOT_CONNECTED, null);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOT_CONNECTED, null);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOT_CONNECTED, null);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOT_CONNECTED, null);
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        responseHandler.onError(Error.NOT_CONNECTED, null);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void enableCommunication() {
    }

    @Override
    public void disableCommunication() {
    }
}
