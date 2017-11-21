/*
* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.devicesetup;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

class SampleAppliance extends Appliance {

    static final String DEVICE_TYPE = "";

    SampleAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
    }

    @Override
    public String getDeviceType() {
        return DEVICE_TYPE;
    }
}
