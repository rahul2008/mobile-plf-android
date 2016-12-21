/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.context;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public interface TransportContext {
    DiscoveryStrategy getDiscoveryStrategy();

    CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode);
}
