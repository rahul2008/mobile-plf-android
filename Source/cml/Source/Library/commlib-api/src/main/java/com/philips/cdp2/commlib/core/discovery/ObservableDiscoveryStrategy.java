/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.discovery;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class ObservableDiscoveryStrategy implements DiscoveryStrategy {
    private final Set<DiscoveryStrategy.DiscoveryListener> discoveryListeners = new CopyOnWriteArraySet<>();

    public void addDiscoveryListener(@NonNull DiscoveryStrategy.DiscoveryListener discoveryListener) {
        this.discoveryListeners.add(discoveryListener);
    }

    public void removeDiscoveryListener(@NonNull DiscoveryStrategy.DiscoveryListener discoveryListener) {
        this.discoveryListeners.remove(discoveryListener);
    }

    protected void notifyDiscoveryStarted() {
        for (DiscoveryStrategy.DiscoveryListener listener : discoveryListeners) {
            listener.onDiscoveryStarted();
        }
    }

    protected void notifyNetworkNodeDiscovered(@NonNull NetworkNode networkNode) {
        for (DiscoveryStrategy.DiscoveryListener listener : discoveryListeners) {
            listener.onNetworkNodeDiscovered(networkNode);
        }
    }

    protected void notifyNetworkNodeLost(@NonNull NetworkNode networkNode) {
        for (DiscoveryStrategy.DiscoveryListener listener : discoveryListeners) {
            listener.onNetworkNodeLost(networkNode);
        }
    }

    protected void notifyDiscoveryStopped() {
        for (DiscoveryStrategy.DiscoveryListener listener : discoveryListeners) {
            listener.onDiscoveryStopped();
        }
    }
}
