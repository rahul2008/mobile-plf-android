/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.commlib.cloud.context;

import android.support.annotation.NonNull;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.commlib.cloud.communication.CloudCommunicationStrategy;
import com.philips.commlib.core.context.TransportContext;
import com.philips.commlib.core.discovery.DiscoveryStrategy;

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
