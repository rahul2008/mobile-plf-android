/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.cloud.context;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;

public class CloudTransportContext implements TransportContext {
    private CloudController cloudController;

    public CloudTransportContext(@NonNull final CloudController cloudController) {
        this.cloudController = cloudController;
    }

    @Override
    public DiscoveryStrategy getDiscoveryStrategy() {
        return null;
    }

    @Override
    public CloudCommunicationStrategy createCommunicationStrategyFor(@NonNull NetworkNode networkNode) {
        return new CloudCommunicationStrategy(networkNode, this.cloudController);
    }
}
