/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample.airpurifier;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public class ComfortAirPurifier extends AirPurifier<ComfortAirPort> {

    public ComfortAirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new ComfortAirPort(networkNode, communicationStrategy);
        addPort(airPort);
    }
}