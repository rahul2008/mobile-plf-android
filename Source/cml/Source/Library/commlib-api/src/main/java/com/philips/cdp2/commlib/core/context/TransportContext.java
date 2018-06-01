/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.context;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;

/**
 * Context for a transport layer.
 * <p>
 * Used to get the {@link DiscoveryStrategy} for a transport layer and make {@link CommunicationStrategy}s
 * for {@link Appliance}s using that transport.
 *
 * @publicApi
 */
public interface TransportContext {

    /**
     * Get the {@link DiscoveryStrategy} for this transport.
     * <p>
     * Typically this will always return the same object.
     *
     * @return the {@link DiscoveryStrategy} for this transport
     */
    @Nullable
    DiscoveryStrategy getDiscoveryStrategy();

    /**
     * Create a communication strategy for the provided {@link NetworkNode}.
     *
     * @param networkNode the network node
     * @return the communication strategy
     */
    @NonNull
    CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode);
}
