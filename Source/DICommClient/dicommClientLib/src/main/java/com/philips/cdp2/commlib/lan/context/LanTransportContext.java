/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.context;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.lan.NetworkMonitor;
import com.philips.cdp2.commlib.lan.communication.LanCommunicationStrategy;
import com.philips.cdp2.commlib.lan.discovery.LanDiscoveryStrategy;

import java.util.HashSet;
import java.util.Set;
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

    private ScheduledThreadPoolExecutor createThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(2);
    }

    public static void rejectNewPinFor(final @NonNull Appliance appliance) {
        appliance.getNetworkNode().setPin(null);
    }

    public static void acceptNewPinFor(final @NonNull Appliance appliance) {
        final NetworkNode networkNode = appliance.getNetworkNode();

        networkNode.setPin(networkNode.getMismatchedPin());
        networkNode.setMismatchedPin(null);
    }

    @NonNull
    public static <A extends Appliance> Set<A> findAppliancesWithMismatchedPinIn(final @NonNull Set<A> appliances) {
        Set<A> appliancesWithMismatchedPin = new HashSet<>();

        for (A appliance : appliances) {
            if (appliance.getNetworkNode().getMismatchedPin() != null) {
                appliancesWithMismatchedPin.add(appliance);
            }
        }

        return appliancesWithMismatchedPin;
    }
}
