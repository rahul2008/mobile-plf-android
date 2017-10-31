/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.appliance.brighteyes;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.demouapp.port.brighteyes.WakeUpAlarmPort;

public class BrightEyesAppliance extends Appliance {

    public static final String DEVICETYPE = "Wake-up Light";

    private final WakeUpAlarmPort wakeUpAlarmPort;

    public BrightEyesAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);

        wakeUpAlarmPort = new WakeUpAlarmPort(communicationStrategy);
        addPort(wakeUpAlarmPort);
    }

    @Override
    public String getDeviceType() {
        return DEVICETYPE;
    }

    public WakeUpAlarmPort getWakeUpAlarmPort() {
        return wakeUpAlarmPort;
    }
}
