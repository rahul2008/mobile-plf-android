/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.appliance.airpurifier;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.devicetest.CombinedCommunicationTestingStrategy;
import com.philips.cdp2.commlib.devicetest.port.air.JaguarAirPort;

/**
 * The type Jaguar AirPurifier.
 * <p>
 * IMPORTANT: This class of AirPurifier doesn't advertise any SSDP modelNumber (which maps to {@link NetworkNode#getModelId()})
 * </p>
 */
public class JaguarAirPurifier extends AirPurifier {

    public JaguarAirPurifier(final @NonNull NetworkNode networkNode, @NonNull CombinedCommunicationTestingStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new JaguarAirPort(communicationStrategy);
        addPort(airPort);
    }
}
