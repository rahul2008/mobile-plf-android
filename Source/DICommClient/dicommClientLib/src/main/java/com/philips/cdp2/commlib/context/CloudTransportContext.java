/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.context;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.communication.CloudCommunicationStrategy;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class CloudTransportContext implements TransportContext {
    private CloudController cloudController;

    public CloudTransportContext(@NonNull final CloudController cloudController) {
        this.cloudController = cloudController;
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        throw new UnsupportedOperationException("CloudTransportContext does not support discovery.");
    }

    @Override
    public CommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new CloudCommunicationStrategy(networkNode, this.cloudController);
    }
}
