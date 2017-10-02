/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.appliance;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BEApplianceFactory implements DICommApplianceFactory<BEAppliance> {

    private static final String TAG = "BEApplianceFactory";

    @NonNull
    private final LanTransportContext lanTransportContext;

    public BEApplianceFactory(@NonNull final LanTransportContext lanTransportContext) {
        this.lanTransportContext = lanTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        boolean isBrightEyesModel = getSupportedDeviceTypes().contains(networkNode.getDeviceType());
        return isBrightEyesModel;
    }

    @Override
    public BEAppliance createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            CommunicationStrategy communicationStrategy = createCommunicationStrategy(networkNode);
            return createAppliance(networkNode, communicationStrategy);
        }
        return null;
    }

    @Override
    public Set<String> getSupportedDeviceTypes() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(BEAppliance.DEVICE_TYPE);
        }});
    }

    @VisibleForTesting
    @NonNull
    BEAppliance createAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        return new BEAppliance(networkNode, communicationStrategy);
    }

    @NonNull
    private CommunicationStrategy createCommunicationStrategy(NetworkNode networkNode) {
        return lanTransportContext.createCommunicationStrategyFor(networkNode);
    }
}