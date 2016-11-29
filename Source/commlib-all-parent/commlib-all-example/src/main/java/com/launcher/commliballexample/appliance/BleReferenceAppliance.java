package com.launcher.commliballexample.appliance;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class BleReferenceAppliance extends DICommAppliance {

    public static final String MODELNAME = "ReferenceNode";

    public BleReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }
}
