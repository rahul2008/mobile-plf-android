/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.common.WiFiNetworksPort;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

@SuppressWarnings("WeakerAccess")
public class EWSGenericAppliance extends Appliance {

    @NonNull
    private final WiFiNetworksPort wiFiNetworksPort;

    public EWSGenericAppliance(final NetworkNode networkNode, final CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
        wiFiNetworksPort = new WiFiNetworksPort(communicationStrategy);
        addPort(wiFiNetworksPort);
    }

    @Override
    public String getDeviceType() {
        return null;
    }

    @NonNull
    WiFiNetworksPort getWiFiNetworksPort() {
        return wiFiNetworksPort;
    }
}