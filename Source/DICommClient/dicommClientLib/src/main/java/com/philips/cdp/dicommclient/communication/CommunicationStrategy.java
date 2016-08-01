/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.communication;

import java.util.HashMap;
import java.util.Map;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;

public abstract class CommunicationStrategy {

    public static final String SUBSCRIBER_KEY = "subscriber";
    public static final String TTL_KEY = "ttl";

    public abstract void getProperties(String portName, int productId, ResponseHandler responseHandler);

    public abstract void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    public abstract void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    public abstract void deleteProperties(String portName, int productId, ResponseHandler responseHandler);

    public abstract void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler);

    public abstract void unsubscribe(String portName, int productId, ResponseHandler responseHandler);

    public abstract boolean isAvailable();

    public abstract void enableSubscription(SubscriptionEventListener subscriptionEventListener);

    public abstract void disableCommunication();

    protected Map<String, Object> getSubscriptionData(int subscriptionTtl) {
        Map<String, Object> dataMap = getUnsubscriptionData();
        dataMap.put(TTL_KEY, subscriptionTtl);
        return dataMap;
    }

    protected Map<String, Object> getUnsubscriptionData() {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(SUBSCRIBER_KEY, DICommClientWrapper.getAppId());
        return dataMap;
    }
}
