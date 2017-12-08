/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.airpurifier;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.demouapp.port.air.JaguarAirPort;

/**
 * The type Jaguar AirPurifier.
 * <p>
 * IMPORTANT: This class of AirPurifier doesn't advertise any SSDP modelNumber (which maps to {@link NetworkNode#getModelId()})
 * </p>
 */
public class JaguarAirPurifier extends AirPurifier {

    public JaguarAirPurifier(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new JaguarAirPort(communicationStrategy);
        addPort(airPort);
    }
}
