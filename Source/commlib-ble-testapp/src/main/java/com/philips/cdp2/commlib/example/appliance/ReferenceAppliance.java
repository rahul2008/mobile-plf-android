/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.example.port.time.TimePort;

public abstract class ReferenceAppliance extends Appliance {
    public static final String CPPID = "cppid";

    private final TimePort timePort;

    public ReferenceAppliance(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        timePort = new TimePort(communicationStrategy);
        addPort(timePort);
    }

    public TimePort getTimePort() {
        return timePort;
    }
}
