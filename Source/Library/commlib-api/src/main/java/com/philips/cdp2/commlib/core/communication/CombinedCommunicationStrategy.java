/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.communication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp2.commlib.core.util.Availability;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * A {@link CommunicationStrategy} that combines multiple CommunicationStrategies.
 * <p>
 * CommunicationStrategies supplied to CombinedCommunicationStrategy are used in order. If the first
 * CommunicationStrategy is not available the second one is used, and so on. If no available
 * CommunicationStrategy can be found all calls will return errors.
 *
 * @publicApi
 */
public class CombinedCommunicationStrategy extends CommunicationStrategy {

    @NonNull
    private final LinkedHashSet<CommunicationStrategy> communicationStrategies;

    @Nullable
    private CommunicationStrategy lastPreferredStrategy;

    @NonNull
    private final NullCommunicationStrategy nullStrategy = new NullCommunicationStrategy();

    public CombinedCommunicationStrategy(@NonNull CommunicationStrategy... communicationStrategies) {
        this.communicationStrategies = new LinkedHashSet<>(Arrays.asList(communicationStrategies));
        if (this.communicationStrategies.isEmpty()) {
            throw new IllegalArgumentException("CombinedCommunicationStrategy needs to be constructed with at least 1 communication strategy.");
        }

        lastPreferredStrategy = firstAvailableStrategy();
        notifyAvailabilityChanged();

        for (CommunicationStrategy c : communicationStrategies) {
            c.addAvailabilityListener(new AvailabilityListener<CommunicationStrategy>() {
                @Override
                public void onAvailabilityChanged(@NonNull CommunicationStrategy object) {
                    CommunicationStrategy newStrategy = firstAvailableStrategy();
                    if (newStrategy != lastPreferredStrategy) {
                        notifyAvailabilityChanged();
                    }
                }
            });
        }
    }

    @Override
    public void getProperties(String portName, int productId, ResponseHandler responseHandler) {
        findStrategy().getProperties(portName, productId, responseHandler);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        findStrategy().putProperties(dataMap, portName, productId, responseHandler);
    }

    @Override
    public void addProperties(Map<String, Object> dataMap, String portName,
                              int productId, ResponseHandler responseHandler) {
        findStrategy().addProperties(dataMap, portName, productId, responseHandler);
    }

    @Override
    public void deleteProperties(String portName, int productId, ResponseHandler responseHandler) {
        findStrategy().deleteProperties(portName, productId, responseHandler);
    }

    @Override
    public void subscribe(String portName, int productId, int subscriptionTtl, ResponseHandler responseHandler) {
        findStrategy().subscribe(portName, productId, subscriptionTtl, responseHandler);
    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        findStrategy().unsubscribe(portName, productId, responseHandler);
    }

    /**
     * Determines if this {@link CommunicationStrategy} is available.
     *
     * @return true if any of the underlying CommunicationStrategies are available.
     * @see Availability#isAvailable()
     */
    @Override
    public boolean isAvailable() {
        return firstAvailableStrategy() != null;
    }

    @Override
    public void enableCommunication(SubscriptionEventListener subscriptionEventListener) {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.enableCommunication(subscriptionEventListener);
        }
    }

    @Override
    public void disableCommunication() {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.disableCommunication();
        }
    }

    @NonNull
    private CommunicationStrategy findStrategy() {
        final CommunicationStrategy strategy = firstAvailableStrategy();
        if (strategy == null) {
            return nullStrategy;
        }
        return strategy;
    }

    @Nullable
    private CommunicationStrategy firstAvailableStrategy() {
        for (CommunicationStrategy strategy : communicationStrategies) {
            if (strategy.isAvailable()) {
                return strategy;
            }
        }
        return null;
    }
}
