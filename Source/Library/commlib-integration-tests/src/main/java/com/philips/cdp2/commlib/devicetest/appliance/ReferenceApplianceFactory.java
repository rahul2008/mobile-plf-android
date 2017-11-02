/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.cloud.context.CloudTransportContext;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.CombinedCommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public final class ReferenceApplianceFactory implements ApplianceFactory {
    @NonNull
    private final BleTransportContext bleTransportContext;
    @NonNull
    private final LanTransportContext lanTransportContext;
    @NonNull
    private final CloudTransportContext cloudTransportContext;

    public ReferenceApplianceFactory(@NonNull BleTransportContext bleTransportContext,
                                     @NonNull LanTransportContext lanTransportContext,
                                     @NonNull CloudTransportContext cloudTransportContext) {
        this.bleTransportContext = bleTransportContext;
        this.lanTransportContext = lanTransportContext;
        this.cloudTransportContext = cloudTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return networkNode.isValid();
    }

    @Override
    public Appliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {

            final CommunicationStrategy communicationStrategy = new CombinedCommunicationStrategy(
                    bleTransportContext.createCommunicationStrategyFor(networkNode),
                    lanTransportContext.createCommunicationStrategyFor(networkNode),
                    cloudTransportContext.createCommunicationStrategyFor(networkNode));

            switch (networkNode.getDeviceType()) {
                case BleReferenceAppliance.DEVICETYPE:
                    return new BleReferenceAppliance(networkNode, communicationStrategy);
                case WifiReferenceAppliance.DEVICETYPE:
                    return new WifiReferenceAppliance(networkNode, communicationStrategy);
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedDeviceTypes() {
        return unmodifiableSet(new HashSet<String>());
    }
}
