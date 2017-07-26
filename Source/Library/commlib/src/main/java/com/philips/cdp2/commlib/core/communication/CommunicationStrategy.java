/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.core.util.AppIdProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class CommunicationStrategy {

    public static final String SUBSCRIBER_KEY = "subscriber";
    public static final String TTL_KEY = "ttl";

    private final Set<SubscriptionEventListener> subscriptionEventListeners = new CopyOnWriteArraySet<>();

    public abstract void getProperties(String portName, int productId, ResponseHandler responseHandler);

    public abstract void putProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    public abstract void addProperties(Map<String, Object> dataMap, String portName, int productId, ResponseHandler responseHandler);

    public abstract void deleteProperties(String portName, int productId, ResponseHandler responseHandler);

    public abstract void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler);

    public abstract void unsubscribe(String portName, int productId, ResponseHandler responseHandler);

    public abstract boolean isAvailable();

    public abstract void enableCommunication(SubscriptionEventListener subscriptionEventListener);

    public abstract void disableCommunication();

    public void addSubscriptionEventListener(@NonNull final SubscriptionEventListener listener) {
        subscriptionEventListeners.add(listener);
    }

    public void removeSubscriptionEventListener(@NonNull final SubscriptionEventListener listener) {
        subscriptionEventListeners.remove(listener);
    }

    protected void notifySubscriptionEventListeners(@NonNull final String data) {
        for (SubscriptionEventListener listener : subscriptionEventListeners) {
            listener.onSubscriptionEventReceived(data);
        }
    }

    protected Map<String, Object> getSubscriptionData(int subscriptionTtl) {
        Map<String, Object> dataMap = getUnsubscriptionData();
        dataMap.put(TTL_KEY, subscriptionTtl);
        return dataMap;
    }

    protected Map<String, Object> getUnsubscriptionData() {
        return new HashMap<String, Object>() {{
            put(SUBSCRIBER_KEY, AppIdProvider.getAppId());
        }};
    }
}
