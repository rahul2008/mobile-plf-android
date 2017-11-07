/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.appliance.airpurifier;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.devicetest.CombinedCommunicationTestingStrategy;
import com.philips.cdp2.commlib.devicetest.appliance.BaseAppliance;
import com.philips.cdp2.commlib.devicetest.port.air.AirPort;
import com.philips.cdp2.commlib.devicetest.port.air.AirPortProperties;

public abstract class AirPurifier extends BaseAppliance {

    public static final String DEVICETYPE = "AirPurifier";
    AirPort<? extends AirPortProperties> airPort;

    public AirPurifier(@NonNull NetworkNode networkNode, @NonNull CombinedCommunicationTestingStrategy communicationStrategy) {
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
