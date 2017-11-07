/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.devicetest.CombinedCommunicationTestingStrategy;
import com.philips.cdp2.commlib.devicetest.port.time.TimePort;

public abstract class ReferenceAppliance extends BaseAppliance {

    private final TimePort timePort;


    public ReferenceAppliance(NetworkNode networkNode, final @NonNull CombinedCommunicationTestingStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        timePort = new TimePort(communicationStrategy);
        addPort(timePort);
    }

    public TimePort getTimePort() {
        return timePort;
    }


}
