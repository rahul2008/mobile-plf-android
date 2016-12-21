/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.CommunicationMarshal;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclientsample.airpurifier.AirPurifier;
import com.philips.cdp.dicommclientsample.airpurifier.ComfortAirPurifier;
import com.philips.cdp.dicommclientsample.airpurifier.JaguarAirPurifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class SampleApplianceFactory implements DICommApplianceFactory<AirPurifier> {

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return AirPurifier.MODELNAME.equals(networkNode.getModelName());
    }

    @Override
    public AirPurifier createApplianceForNode(NetworkNode networkNode) {
        if (networkNode.getModelName().equals(AirPurifier.MODELNAME)) {
            CommunicationStrategy communicationStrategy = new CommunicationMarshal(new DISecurity(networkNode), networkNode);
            if (ComfortAirPurifier.MODELNUMBER.equals(networkNode.getModelType())) {
                return new ComfortAirPurifier(networkNode, communicationStrategy);
            }
            return new JaguarAirPurifier(networkNode, communicationStrategy);
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
