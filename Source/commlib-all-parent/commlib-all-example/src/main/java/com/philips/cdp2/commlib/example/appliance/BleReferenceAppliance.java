/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example.appliance;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class BleReferenceAppliance extends DICommAppliance {

    public static final String MODELNAME = "ReferenceNode";
    private final TimePort timePort;

    public BleReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        timePort = new TimePort(networkNode, communicationStrategy);
        addPort(timePort);
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }

    public TimePort getTimePort() {
        return timePort;
    }
}
