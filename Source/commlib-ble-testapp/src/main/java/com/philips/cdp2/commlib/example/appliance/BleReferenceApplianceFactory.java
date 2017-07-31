/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class BleReferenceApplianceFactory implements DICommApplianceFactory<BleReferenceAppliance> {
    @NonNull
    private final BleTransportContext bleTransportContext;

    public BleReferenceApplianceFactory(@NonNull BleTransportContext bleTransportContext) {
        this.bleTransportContext = bleTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return getSupportedDeviceTypes().contains(networkNode.getDeviceType());
    }

    @Override
    public BleReferenceAppliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {

            switch (networkNode.getDeviceType()) {
                case BleReferenceAppliance.DEVICETYPE:
                    final CommunicationStrategy bleCommunicationStrategy = bleTransportContext.createCommunicationStrategyFor(networkNode);
                    return new BleReferenceAppliance(networkNode, bleCommunicationStrategy);
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedDeviceTypes() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(BleReferenceAppliance.DEVICETYPE);
        }});
    }
}
