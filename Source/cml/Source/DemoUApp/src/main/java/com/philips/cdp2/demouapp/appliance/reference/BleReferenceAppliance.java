/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.reference;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.demouapp.port.ble.BleParamsPort;

public class BleReferenceAppliance extends ReferenceAppliance {

    public static final String DEVICETYPE = "ReferenceNode";

    private final BleParamsPort bleParamsPort;

    public BleReferenceAppliance(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        bleParamsPort = new BleParamsPort(communicationStrategy);
        addPort(bleParamsPort);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }

    public BleParamsPort getBleParamsPort() {
        return bleParamsPort;
    }
}
