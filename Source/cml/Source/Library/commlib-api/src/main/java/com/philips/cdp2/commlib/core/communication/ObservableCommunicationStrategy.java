/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.core.CommCentral;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class ObservableCommunicationStrategy implements CommunicationStrategy {

    private static final String SUBSCRIBER_KEY = "subscriber";
    private static final String TTL_KEY = "ttl";

    private Set<AvailabilityListener<CommunicationStrategy>> availabilityListeners = new CopyOnWriteArraySet<>();

    protected final Set<SubscriptionEventListener> subscriptionEventListeners = new CopyOnWriteArraySet<>();

    @Override
    public void addSubscriptionEventListener(@NonNull final SubscriptionEventListener listener) {
        subscriptionEventListeners.add(listener);
    }

    @Override
    public void removeSubscriptionEventListener(@NonNull final SubscriptionEventListener listener) {
        subscriptionEventListeners.remove(listener);
    }

    protected void notifySubscriptionEventListeners(@NonNull final String portName, @NonNull final String data) {
        for (SubscriptionEventListener listener : subscriptionEventListeners) {
            listener.onSubscriptionEventReceived(portName, data);
        }
    }

    // TODO move
    protected Map<String, Object> getSubscriptionData(int subscriptionTtl) {
        Map<String, Object> dataMap = getUnsubscriptionData();
        dataMap.put(TTL_KEY, subscriptionTtl);
        return dataMap;
    }

    // TODO move
    protected Map<String, Object> getUnsubscriptionData() {
        return new HashMap<String, Object>() {{
            put(SUBSCRIBER_KEY, CommCentral.getAppIdProvider().getAppId());
        }};
    }

    protected void notifyAvailabilityChanged() {
        for (AvailabilityListener<CommunicationStrategy> listener : availabilityListeners) {
            listener.onAvailabilityChanged(this);
        }
    }

    @Override
    public void addAvailabilityListener(@NonNull AvailabilityListener<CommunicationStrategy> listener) {
        availabilityListeners.add(listener);
        listener.onAvailabilityChanged(this);
    }

    @Override
    public void removeAvailabilityListener(@NonNull AvailabilityListener<CommunicationStrategy> listener) {
        availabilityListeners.remove(listener);
    }
}
