/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.reference;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class BleReferenceAppliance extends ReferenceAppliance {

    public static final String DEVICETYPE = "ReferenceNode";

    public BleReferenceAppliance(final @NonNull NetworkNode networkNode, final @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }
}
