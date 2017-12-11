/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class BEAppliance extends Appliance {

    static final String DEVICE_TYPE = "Wake-up Light";
    static final String PRODUCT_STUB = "BCM943903";

    BEAppliance(@NonNull final NetworkNode networkNode, @NonNull final CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICE_TYPE;
    }
}