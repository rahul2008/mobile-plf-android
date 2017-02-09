/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.commlib.lan.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.commlib.core.context.TransportContext;
import com.philips.commlib.core.discovery.DiscoveryStrategy;
import com.philips.commlib.lan.NetworkMonitor;
import com.philips.commlib.lan.communication.LanCommunicationStrategy;
import com.philips.commlib.lan.discovery.LanDiscoveryStrategy;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class LanTransportContext implements TransportContext {

    @NonNull
    private final DiscoveryStrategy discoveryStrategy;

    public LanTransportContext(@NonNull final Context context) {
        final ScheduledThreadPoolExecutor executor = createThreadPoolExecutor();
        final NetworkMonitor networkMonitor = new NetworkMonitor(context, executor);
        this.discoveryStrategy = new LanDiscoveryStrategy(networkMonitor);
    }

    @Override
    @NonNull
    public DiscoveryStrategy getDiscoveryStrategy() {
        return this.discoveryStrategy;
    }

    @Override
    @NonNull
    public LanCommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new LanCommunicationStrategy(networkNode);
    }

    protected ScheduledThreadPoolExecutor createThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(2);
    }
}
