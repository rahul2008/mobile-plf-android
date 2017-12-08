/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.airpurifier;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.demouapp.port.air.AirPort;
import com.philips.cdp2.demouapp.port.air.AirPortProperties;

public abstract class AirPurifier extends Appliance {

    public static final String DEVICETYPE = "AirPurifier";
    AirPort<? extends AirPortProperties> airPort;

    AirPurifier(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }

    public AirPort<?> getAirPort() {
        return airPort;
    }
}
