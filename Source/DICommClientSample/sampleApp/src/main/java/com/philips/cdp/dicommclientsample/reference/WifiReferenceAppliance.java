/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclientsample.reference;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.time.TimePort;

public class WifiReferenceAppliance extends Appliance {

    public static final String DEVICETYPE = "BCM943903";
    private final TimePort timePort;

    public WifiReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
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
