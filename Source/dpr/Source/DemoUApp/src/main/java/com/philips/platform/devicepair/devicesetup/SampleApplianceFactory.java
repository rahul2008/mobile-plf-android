/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.devicesetup;

import android.support.annotation.NonNull;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.CombinedCommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

public class SampleApplianceFactory implements ApplianceFactory {

    @NonNull
    private final LanTransportContext lanTransportContext;

    public SampleApplianceFactory(@NonNull final LanTransportContext lanTransportContext) {
        this.lanTransportContext = lanTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(@NonNull NetworkNode networkNode) {
        return networkNode.isValid();
    }

    @Override
    public Appliance createApplianceForNode(@NonNull NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = new CombinedCommunicationStrategy(
                    lanTransportContext.createCommunicationStrategyFor(networkNode));

            switch (networkNode.getDeviceType()) {
                case SampleAppliance.DEVICE_TYPE:
                    return new SampleAppliance(networkNode, communicationStrategy);
                default:
                    return new Appliance(networkNode, communicationStrategy) {
                        @Override
                        public String getDeviceType() {
                            return null;
                        }
                    };
            }
        }
        return null;
    }
}
