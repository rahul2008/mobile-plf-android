/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.NullCommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.ObservableCommunicationStrategy;
import com.philips.cdp2.commlib.core.util.Availability;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A {@link CommunicationStrategy} that combines multiple CommunicationStrategies.
 * <p>
 * CommunicationStrategies supplied to CombinedCommunicationStrategy are used in order. If the first
 * {@link CommunicationStrategy} is not available the second one is used, and so on. If no available
 * {@link CommunicationStrategy} can be found all calls will return errors.
 *
 * @publicApi
 */
public class CombinedCommunicationTestingStrategy extends ObservableCommunicationStrategy {

    public static final String TAG = "COMBINED_STRATEGY";
    @NonNull
    private final LinkedHashSet<CommunicationStrategy> communicationStrategies;

    @Nullable
    private CommunicationStrategy lastPreferredStrategy;

    @NonNull
    private final NullCommunicationStrategy nullStrategy = new NullCommunicationStrategy();

    @Nullable
    private CommunicationStrategy forced;

    private static class Subscription {

        @NonNull
        private final String portname;
        private final int productId;
        private final int ttl;

        private Subscription(@NonNull String portname, int productId, int ttl) {
            this.portname = portname;
            this.productId = productId;
            this.ttl = ttl;
        }

        private void subscribe(@Nullable CommunicationStrategy strategy, @NonNull ResponseHandler handler) {
            if (strategy != null) {
                strategy.subscribe(portname, productId, ttl, handler);
            }
        }

        private void unsubscribe(@Nullable CommunicationStrategy strategy, @NonNull ResponseHandler handler) {
            if (strategy != null) {
                strategy.unsubscribe(portname, productId, handler);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Subscription that = (Subscription) o;

            if (productId != that.productId) return false;
            return portname.equals(that.portname);

        }

        @Override
        public int hashCode() {
            int result = portname.hashCode();
            result = 31 * result + productId;
            return result;
        }
    }

    private Set<Subscription> subscriptions = new CopyOnWriteArraySet<>();

    public CombinedCommunicationTestingStrategy(@NonNull CommunicationStrategy... communicationStrategies) {
        this.communicationStrategies = new LinkedHashSet<>(Arrays.asList(communicationStrategies));
        if (this.communicationStrategies.isEmpty()) {
            throw new IllegalArgumentException("CombinedCommunicationStrategy needs to be constructed with at least 1 communication strategy.");
        }

        lastPreferredStrategy = firstAvailableStrategy();

        for (CommunicationStrategy c : communicationStrategies) {
            c.addAvailabilityListener(new AvailabilityListener<CommunicationStrategy>() {
                @Override
                public void onAvailabilityChanged(@NonNull CommunicationStrategy object) {
                    CommunicationStrategy newStrategy = firstAvailableStrategy();
                    if (newStrategy != lastPreferredStrategy) {
                        DICommLog.v(TAG, "Switched to strategy " + (newStrategy != null ? newStrategy.toString() : "null"));

                        if (newStrategy == null || lastPreferredStrategy == null) {
                            notifyAvailabilityChanged();
                        }

                        ResponseHandler handler = new ResponseHandler() {
                            @Override
                            public void onSuccess(String data) {

                            }

                            @Override
                            public void onError(Error error, String errorData) {

                            }
                        };

                        for (Subscription sub : subscriptions) {
                            sub.unsubscribe(lastPreferredStrategy, handler);
                            sub.subscribe(newStrategy, handler);
                        }

                        lastPreferredStrategy = newStrategy;
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
    public void subscribe(String portName, int productId, int subscriptionTtl, final ResponseHandler responseHandler) {
        if (isAvailable()) {
            final Subscription sub = new Subscription(portName, productId, subscriptionTtl);
            sub.subscribe(findStrategy(), new ResponseHandler() {
                @Override
                public void onSuccess(String data) {
                    CombinedCommunicationTestingStrategy.this.subscriptions.add(sub);
                    responseHandler.onSuccess(data);
                }

                @Override
                public void onError(Error error, String errorData) {
                    responseHandler.onError(error, errorData);
                }
            });
        } else {
            responseHandler.onError(Error.NOT_CONNECTED, "Appliance is not connected");
        }

    }

    @Override
    public void unsubscribe(String portName, int productId, ResponseHandler responseHandler) {
        Subscription removed = new Subscription(portName, productId, 0);
        subscriptions.remove(removed);
        removed.unsubscribe(findStrategy(), responseHandler);
    }

    /**
     * Determines if this {@link ObservableCommunicationStrategy} is available.
     *
     * @return true if any of the underlying CommunicationStrategies are available.
     * @see Availability#isAvailable()
     */
    @Override
    public boolean isAvailable() {
        return firstAvailableStrategy() != null;
    }

    @Override
    public void enableCommunication() {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.enableCommunication();
        }
    }

    @Override
    public void disableCommunication() {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.disableCommunication();
        }
    }

    @NonNull
    public CommunicationStrategy findStrategy() {
        final CommunicationStrategy strategy = firstAvailableStrategy();
        if (strategy == null) {
            DICommLog.v(TAG, "No strategy is available");
            return nullStrategy;
        }
        DICommLog.v(TAG, "Using strategy " + strategy.toString());
        return strategy;
    }

    @Nullable
    private CommunicationStrategy firstAvailableStrategy() {
        if (forced != null) {
            return forced;
        }
        for (CommunicationStrategy strategy : communicationStrategies) {
            if (strategy.isAvailable()) {

                return strategy;
            }
        }
        return null;
    }

    @Override
    public void addSubscriptionEventListener(@NonNull SubscriptionEventListener listener) {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.addSubscriptionEventListener(listener);
        }
    }

    @Override
    public void removeSubscriptionEventListener(@NonNull SubscriptionEventListener listener) {
        for (CommunicationStrategy strategy : communicationStrategies) {
            strategy.removeSubscriptionEventListener(listener);
        }
    }

    public void forceStrategyType(Class<? extends CommunicationStrategy> strategyType) {
        forced = null;
        for (CommunicationStrategy strategy : communicationStrategies) {
            if (strategy.getClass() == strategyType) {
                forced = strategy;
            }
        }
        notifyAvailabilityChanged();
    }
}
