package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.CommunicationMarshal;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclientsample.airpurifier.AirPurifier;
import com.philips.cdp.dicommclientsample.airpurifier.ComfortAirPurifier;
import com.philips.cdp.dicommclientsample.airpurifier.JaguarAirPurifier;

/**
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
class SampleApplianceFactory extends DICommApplianceFactory<AirPurifier> {

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        if (networkNode.getModelName().equals(AirPurifier.MODELNAME)) {
            // Optionally add a check for the modeltype and return a different
            // DICommAppliance depending on the type.
            return true;
        }
        return false;
    }

    @Override
    public AirPurifier createApplianceForNode(NetworkNode networkNode) {
        if (networkNode.getModelName().equals(ComfortAirPurifier.MODELNAME)) {
            CommunicationStrategy communicationStrategy = new CommunicationMarshal(new DISecurity());
            if ("AC2889".equals(networkNode.getModelType())) {
                return new ComfortAirPurifier(networkNode, communicationStrategy);
            }
            return new JaguarAirPurifier(networkNode, communicationStrategy);
        }
        return null;
    }
}
