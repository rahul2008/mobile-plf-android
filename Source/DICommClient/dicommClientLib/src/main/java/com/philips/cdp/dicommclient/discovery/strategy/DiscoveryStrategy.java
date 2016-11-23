/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.discovery.strategy;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

public interface DiscoveryStrategy {

    interface NetworkNodeListener {
        void onNetworkNodeDiscovered(NetworkNode networkNode);

        void onNetworkNodeLost(NetworkNode networkNode);

        void onNetworkNodeUpdated(NetworkNode networkNode);
    }

    void addNetworkNodeListener(NetworkNodeListener listener);

    void removeNetworkNodeListener(NetworkNodeListener listener);

    void start();

    void stop();

}
