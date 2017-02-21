/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample.airpurifier;

import com.philips.commlib.core.appliance.Appliance;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public abstract class AirPurifier extends Appliance {

    public static final String MODELNAME = "AirPurifier";
    AirPort<?> airPort;

    public AirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }

    public AirPort<?> getAirPort() {
        return airPort;
    }
}
