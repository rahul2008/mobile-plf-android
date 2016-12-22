package com.philips.cdp2.commlib.appliance;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class Mixer extends DICommAppliance {

    public static final String MODELNAME = "mixer";

    public Mixer(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }
}
