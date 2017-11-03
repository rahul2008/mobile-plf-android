/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.devicetest.CombinedCommunicationTestingStrategy;
import com.philips.cdp2.commlib.devicetest.time.TimePort;

public abstract class ReferenceAppliance extends Appliance {

    private final TimePort timePort;
    @NonNull
    private final CombinedCommunicationTestingStrategy communicationStrategy;

    public ReferenceAppliance(NetworkNode networkNode, final @NonNull CombinedCommunicationTestingStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        timePort = new TimePort(communicationStrategy);
        this.communicationStrategy = communicationStrategy;
        addPort(timePort);
    }

    public TimePort getTimePort() {
        return timePort;
    }

    public CombinedCommunicationTestingStrategy getCommunicationStrategy() {
        return communicationStrategy;
    }
}
