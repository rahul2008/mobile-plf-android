/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.communication.BleCommunicationStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BleReferenceApplianceFactory implements DICommApplianceFactory<BleReferenceAppliance> {
    private final BleDeviceCache bleDeviceCache;

    public BleReferenceApplianceFactory(@NonNull BleDeviceCache bleDeviceCache) {
        this.bleDeviceCache = bleDeviceCache;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return BleReferenceAppliance.MODELNAME.equals(networkNode.getModelName());
    }

    @Override
    public BleReferenceAppliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = new BleCommunicationStrategy(networkNode.getCppId(), bleDeviceCache);

            return new BleReferenceAppliance(networkNode, communicationStrategy);
        }
        return null;
    }

    @Override
    public Set<String> getSupportedModelNames() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(BleReferenceAppliance.MODELNAME);
        }});
    }
}
