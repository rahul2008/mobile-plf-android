/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.core.util.Availability;

import java.util.Map;

/**
 * The CommunicationStrategy is responsible for communicating to an actual appliance.
 *
 * @publicApi
 */
public interface CommunicationStrategy extends Availability<CommunicationStrategy> {
    void getProperties(String portName, int productId, ResponseHandler responseHandler);

    void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    void deleteProperties(String portName, int productId, ResponseHandler responseHandler);

    void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler);

    void unsubscribe(String portName, int productId, ResponseHandler responseHandler);

    void addSubscriptionEventListener(@NonNull SubscriptionEventListener listener);

    void removeSubscriptionEventListener(@NonNull SubscriptionEventListener listener);

    @Deprecated
    void enableCommunication();

    @Deprecated
    void disableCommunication();
}
