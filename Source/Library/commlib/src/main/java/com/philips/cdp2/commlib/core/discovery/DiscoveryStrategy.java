/*
 * Copyright (c) 2016, 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.discovery;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

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

    void start() throws MissingPermissionException, TransportUnavailableException;

    void start(@NonNull Set<String> deviceTypes) throws MissingPermissionException, TransportUnavailableException;

    void start(@NonNull Set<String> deviceTypes, @NonNull Set<String> modelIds) throws MissingPermissionException, TransportUnavailableException;

    void stop();

}
