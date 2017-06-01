/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.ReferenceAppliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class BleReferenceAppliance extends ReferenceAppliance {

    public static final String DEVICETYPE = "ReferenceNode";

    public BleReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }
}
