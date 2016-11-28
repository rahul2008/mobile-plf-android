/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

public interface DiscoveryStrategy {

    interface DiscoveryListener {
        void onDiscoveryStarted();

        void onNetworkNodeDiscovered(NetworkNode networkNode);

        void onNetworkNodeLost(NetworkNode networkNode);

        void onNetworkNodeUpdated(NetworkNode networkNode);

        void onDiscoveryFinished();
    }

    void start(DiscoveryListener discoveryListener);

    void stop();

}
