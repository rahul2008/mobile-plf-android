/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cdp.dicommclientsample.airpurifier;

import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

public abstract class AirPurifier<T extends AirPort<?>> extends DICommAppliance {

    public static final String MODELNAME = "AirPurifier";
    T airPort;

    public AirPurifier(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }

    public T getAirPort() {
        return airPort;
    }
}
