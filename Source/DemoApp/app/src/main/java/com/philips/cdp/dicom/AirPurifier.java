package com.philips.cdp.dicom;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

class AirPurifier extends DICommAppliance {

    public static final String MODELNAME = "AirPurifier";
    private final AirPort airPort;

    public AirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new AirPort(networkNode, communicationStrategy);
        addPort(airPort);
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }

    public AirPort getAirPort() {
        return airPort;
    }
}