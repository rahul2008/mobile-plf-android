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

public class BEApplianceFactory implements ApplianceFactory {

    @NonNull
    private final LanTransportContext lanTransportContext;

    public BEApplianceFactory(@NonNull final LanTransportContext lanTransportContext) {
        this.lanTransportContext = lanTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(@NonNull NetworkNode networkNode) {
        final String deviceType = networkNode.getDeviceType();

        return BEAppliance.DEVICE_TYPE.equals(deviceType) || BEAppliance.PRODUCT_STUB.equals(deviceType);
    }

    @Override
    public Appliance createApplianceForNode(@NonNull NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            CommunicationStrategy communicationStrategy = createCommunicationStrategy(networkNode);
            return createAppliance(networkNode, communicationStrategy);
        }
        return null;
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