/*
 * (C) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */
package com.philips.cdp.devicepair.devicesetup;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class JaguarAirPurifier extends AirPurifier {

    public JaguarAirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new JaguarAirPort(communicationStrategy);
        addPort(airPort);
    }
}
