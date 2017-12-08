/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.airpurifier;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.demouapp.port.air.ComfortAirPort;

public class ComfortAirPurifier extends AirPurifier {

    public static final String MODELID = "AC2889";

    public ComfortAirPurifier(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        airPort = new ComfortAirPort(communicationStrategy);
        addPort(airPort);
    }
}
