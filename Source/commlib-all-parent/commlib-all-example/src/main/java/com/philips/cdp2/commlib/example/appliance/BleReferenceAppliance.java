/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.time.TimePort;

public class BleReferenceAppliance extends Appliance {

    public static final String DEVICETYPE = "ReferenceNode";
    private final TimePort timePort;

    public BleReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        timePort = new TimePort(communicationStrategy);
        addPort(timePort);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }

    public TimePort getTimePort() {
        return timePort;
    }
}
