/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.discovery.exception.MissingPermissionException;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import java.util.Collection;

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

    void start(Context context) throws MissingPermissionException;

    void start(Context context, Collection<String> deviceTypes) throws MissingPermissionException;

    void stop();

}
