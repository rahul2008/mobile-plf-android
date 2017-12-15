/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.appliance;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class BEApplianceFactory implements ApplianceFactory {

    @NonNull
    private final LanTransportContext lanTransportContext;

    public BEApplianceFactory(@NonNull final LanTransportContext lanTransportContext) {
        this.lanTransportContext = lanTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(@NonNull NetworkNode networkNode) {
        return getSupportedDeviceTypes().contains(networkNode.getDeviceType());
    }

    @Override
    public Appliance createApplianceForNode(@NonNull NetworkNode networkNode) {
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
            add(BEAppliance.PRODUCT_STUB);
        }});
    }

    @VisibleForTesting
    @NonNull
    BEAppliance createAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy) {
        return new BEAppliance(networkNode, communicationStrategy);
    }

    @NonNull
    private CommunicationStrategy createCommunicationStrategy(@NonNull NetworkNode networkNode) {
        return lanTransportContext.createCommunicationStrategyFor(networkNode);
    }

}