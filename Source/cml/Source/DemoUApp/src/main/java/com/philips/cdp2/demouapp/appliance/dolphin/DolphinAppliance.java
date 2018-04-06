/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.dolphin;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;

public class DolphinAppliance extends BleReferenceAppliance {

    public static final String DEVICETYPE = "CDP";

    public DolphinAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }
}
