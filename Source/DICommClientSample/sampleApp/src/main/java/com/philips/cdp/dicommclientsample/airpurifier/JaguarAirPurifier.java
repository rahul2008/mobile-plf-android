package com.philips.cdp.dicommclientsample.airpurifier;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class JaguarAirPurifier extends AirPurifier<JaguarAirPort> {

    public JaguarAirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new JaguarAirPort(networkNode, communicationStrategy);
        addPort(airPort);
    }
}