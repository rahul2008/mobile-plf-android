/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.context;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.util.Availability;

public interface TransportContext<A extends Availability> extends Availability<A> {
    DiscoveryStrategy getDiscoveryStrategy();

    CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode);
}
