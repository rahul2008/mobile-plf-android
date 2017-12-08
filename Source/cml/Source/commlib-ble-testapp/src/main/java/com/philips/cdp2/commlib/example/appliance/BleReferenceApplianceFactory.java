/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.example.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public final class BleReferenceApplianceFactory implements ApplianceFactory {
    @NonNull
    private final BleTransportContext bleTransportContext;

    public BleReferenceApplianceFactory(@NonNull BleTransportContext bleTransportContext) {
        requireNonNull(bleTransportContext);
        this.bleTransportContext = bleTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(@NonNull NetworkNode networkNode) {
        return getSupportedDeviceTypes().contains(networkNode.getDeviceType());
    }

    @Override
    public Appliance createApplianceForNode(@NonNull NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode) &&
                Objects.equals(networkNode.getDeviceType(), BleReferenceAppliance.DEVICETYPE)) {
            final CommunicationStrategy communicationStrategy = bleTransportContext.createCommunicationStrategyFor(networkNode);
            return new BleReferenceAppliance(networkNode, communicationStrategy);
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
