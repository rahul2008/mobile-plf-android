/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Set;

public interface DiscoveryStrategy {

    interface DiscoveryListener {
        void onDiscoveryStarted();

        void onNetworkNodeDiscovered(NetworkNode networkNode);

        void onNetworkNodeLost(NetworkNode networkNode);

        void onNetworkNodeUpdated(NetworkNode networkNode);

        void onDiscoveryStopped();
    }

    void addDiscoveryListener(@NonNull DiscoveryListener discoveryListener);

    void removeDiscoveryListener(@NonNull DiscoveryListener discoveryListener);

    void start() throws MissingPermissionException;

    void start(Set<String> deviceTypes) throws MissingPermissionException;

    void stop();

}
