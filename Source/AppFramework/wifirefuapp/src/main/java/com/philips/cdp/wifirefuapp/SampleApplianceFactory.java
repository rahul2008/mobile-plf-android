/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.cdp.wifirefuapp;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CombinedCommunicationStrategy;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SampleApplianceFactory implements DICommApplianceFactory<AirPurifier> {

    @NonNull
    private final LanTransportContext lanTransportContext;

    public SampleApplianceFactory(@NonNull final LanTransportContext lanTransportContext) {
        this.lanTransportContext = lanTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return getSupportedModelNames().contains(networkNode.getModelName());
    }

    @Override
    public AirPurifier createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = new CombinedCommunicationStrategy(
                    lanTransportContext.createCommunicationStrategyFor(networkNode));

            if (ComfortAirPurifier.MODELNUMBER.equals(networkNode.getModelId())) {
                return new ComfortAirPurifier(networkNode, communicationStrategy);
            }
            return new ComfortAirPurifier(networkNode, communicationStrategy);
        }
        return null;
    }

    @Override
    public Set<String> getSupportedModelNames() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(AirPurifier.MODELNAME);
        }});
    }
}
