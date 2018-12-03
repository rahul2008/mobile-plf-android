/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.discovery;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class ObservableDiscoveryStrategy implements DiscoveryStrategy {

    private final Handler responseHandler = HandlerProvider.createHandler();

    private final Set<DiscoveryListener> discoveryListeners = new CopyOnWriteArraySet<>();

    public void addDiscoveryListener(@NonNull DiscoveryListener discoveryListener) {
        this.discoveryListeners.add(discoveryListener);
    }

    public void removeDiscoveryListener(@NonNull DiscoveryListener discoveryListener) {
        this.discoveryListeners.remove(discoveryListener);
    }

    protected void notifyDiscoveryStarted() {
        for (final DiscoveryListener listener : discoveryListeners) {
            responseHandler.post(listener::onDiscoveryStarted);
        }
    }

    protected void notifyNetworkNodeDiscovered(@NonNull final NetworkNode networkNode) {
        for (final DiscoveryListener listener : discoveryListeners) {
            responseHandler.post(() -> listener.onNetworkNodeDiscovered(networkNode));
        }
    }

    protected void notifyNetworkNodeLost(@NonNull final NetworkNode networkNode) {
        for (final DiscoveryListener listener : discoveryListeners) {
            responseHandler.post(() -> listener.onNetworkNodeLost(networkNode));
        }
    }

    protected void notifyDiscoveryStopped() {
        for (final DiscoveryListener listener : discoveryListeners) {
            responseHandler.post(listener::onDiscoveryStopped);
        }
    }

    protected void notifyDiscoveryFailedToStart() {
        for (final DiscoveryListener listener : discoveryListeners) {
            responseHandler.post(listener::onDiscoveryFailedToStart);
        }
    }
}
