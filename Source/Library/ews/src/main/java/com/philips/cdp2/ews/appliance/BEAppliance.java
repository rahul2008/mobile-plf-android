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

    public static final String DEVICE_TYPE = "Wake-up Light";
    public static final String PRODUCT_STUB = "BCM943903";
    private static final String TAG = "BEAppliance";

    BEAppliance(@NonNull final NetworkNode networkNode, @NonNull final CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICE_TYPE;
    }
}