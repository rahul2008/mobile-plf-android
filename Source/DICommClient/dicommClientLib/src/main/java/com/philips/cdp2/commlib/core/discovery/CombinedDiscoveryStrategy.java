/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.discovery;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class CombinedDiscoveryStrategy extends ObservableDiscoveryStrategy implements DiscoveryStrategy.DiscoveryListener {

    private static final int MINIMUM_REQUIRED_NUMBER_OF_STRATEGIES = 2;

    private final Set<DiscoveryStrategy> discoveryStrategies = new CopyOnWriteArraySet<>();

    public CombinedDiscoveryStrategy(Set<DiscoveryStrategy> strategies) {
        if (strategies.size() < MINIMUM_REQUIRED_NUMBER_OF_STRATEGIES) {
            throw new IllegalArgumentException(String.format(Locale.US, "A minimum of %d discovery discoveryStrategies is required.", MINIMUM_REQUIRED_NUMBER_OF_STRATEGIES));
        }
        addDiscoveryListener(this);
        this.discoveryStrategies.addAll(strategies);
    }

    @Override
    public void start() throws MissingPermissionException, TransportUnavailableException {
        start(Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> deviceTypes) throws MissingPermissionException, TransportUnavailableException {
        start(deviceTypes, Collections.<String>emptySet());
    }

    @Override
    public void start(@NonNull Set<String> deviceTypes, @NonNull Set<String> modelIds) throws MissingPermissionException, TransportUnavailableException {
        for (DiscoveryStrategy strategy : discoveryStrategies) {
            strategy.start(deviceTypes);
        }
        this.onDiscoveryStarted();
    }

    @Override
    public void stop() {
        for (DiscoveryStrategy strategy : discoveryStrategies) {
            strategy.stop();
        }
        this.onDiscoveryStopped();
    }

    @Override
    public void onDiscoveryStarted() {
        notifyDiscoveryStarted();
    }

    @Override
    public void onNetworkNodeDiscovered(NetworkNode networkNode) {
        notifyNetworkNodeDiscovered(networkNode);
    }

    @Override
    public void onNetworkNodeLost(NetworkNode networkNode) {
        notifyNetworkNodeLost(networkNode);
    }

    @Override
    public void onNetworkNodeUpdated(NetworkNode networkNode) {
        notifyNetworkNodeUpdated(networkNode);
    }

    @Override
    public void onDiscoveryStopped() {
        notifyDiscoveryStopped();
    }
}
