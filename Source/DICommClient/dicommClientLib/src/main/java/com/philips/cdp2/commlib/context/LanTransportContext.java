/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.communication.LanCommunicationStrategy;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.discovery.strategy.LanDiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class LanTransportContext implements TransportContext {

    private DiscoveryStrategy discoveryStrategy;

    public LanTransportContext(@NonNull final Context context) {
        final NetworkMonitor networkMonitor = new NetworkMonitor(context);
        this.discoveryStrategy = new LanDiscoveryStrategy(networkMonitor);
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return this.discoveryStrategy;
    }

    @Override
    public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new LanCommunicationStrategy(networkNode);
    }
}
