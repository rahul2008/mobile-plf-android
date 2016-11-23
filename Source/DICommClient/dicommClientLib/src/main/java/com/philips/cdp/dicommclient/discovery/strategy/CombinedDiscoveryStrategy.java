/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CombinedDiscoveryStrategy implements DiscoveryStrategy {

    private final Set<DiscoveryStrategy> strategies = new CopyOnWriteArraySet<>();
    private final Set<NetworkNodeListener> networkNodeListeners = new CopyOnWriteArraySet<>();

    private final NetworkNodeListener networkNodeListener = new NetworkNodeListener() {
        @Override
        public void onNetworkNodeDiscovered(NetworkNode networkNode) {
            // TODO define combined logic here

            notifyNetworkNodeDiscovered(networkNode);
        }

        @Override
        public void onNetworkNodeLost(NetworkNode networkNode) {
            // TODO define combined logic here

            notifyNetworkNodeLost(networkNode);
        }

        @Override
        public void onNetworkNodeUpdated(NetworkNode networkNode) {
            // TODO define combined logic here

            notifyNetworkNodeUpdated(networkNode);
        }
    };

    public CombinedDiscoveryStrategy(Set<DiscoveryStrategy> strategies) {
        final int minimumRequiredNumberOfStrategies = 2;

        if (strategies.size() < minimumRequiredNumberOfStrategies) {
            throw new IllegalArgumentException(String.format(Locale.US, "A minimum of %d discovery strategies is required.", minimumRequiredNumberOfStrategies));
        }

        for (DiscoveryStrategy strategy : strategies) {
            addDiscoveryStrategy(strategy);
        }
    }

    @Override
    public void addNetworkNodeListener(@NonNull NetworkNodeListener listener) {
        networkNodeListeners.add(listener);
    }

    @Override
    public void removeNetworkNodeListener(@NonNull NetworkNodeListener listener) {
        networkNodeListeners.remove(listener);
    }

    @Override
    public void start() {
        for (DiscoveryStrategy strategy : strategies) {
            strategy.start();
        }
    }

    @Override
    public void stop() {
        for (DiscoveryStrategy strategy : strategies) {
            strategy.stop();
        }
    }

    public void addDiscoveryStrategy(@NonNull DiscoveryStrategy strategy) {
        strategy.addNetworkNodeListener(networkNodeListener);
        strategies.add(strategy);
    }

    public void removeDiscoveryStrategy(@NonNull DiscoveryStrategy strategy) {
        strategy.removeNetworkNodeListener(networkNodeListener);
        strategies.remove(strategy);
    }

    private void notifyNetworkNodeDiscovered(@NonNull NetworkNode networkNode) {
        for (NetworkNodeListener listener : networkNodeListeners) {
            listener.onNetworkNodeDiscovered(networkNode);
        }
    }

    private void notifyNetworkNodeLost(@NonNull NetworkNode networkNode) {
        for (NetworkNodeListener listener : networkNodeListeners) {
            listener.onNetworkNodeLost(networkNode);
        }
    }

    private void notifyNetworkNodeUpdated(@NonNull NetworkNode networkNode) {
        for (NetworkNodeListener listener : networkNodeListeners) {
            listener.onNetworkNodeUpdated(networkNode);
        }
    }
}
