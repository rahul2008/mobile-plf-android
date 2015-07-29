package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.LocalStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
class AirPurifierFactory extends DICommApplianceFactory<AirPurifier> {

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
        if (networkNode.getModelName().equals(AirPurifier.MODELNAME)) {
            LocalStrategy communicationStrategy = new LocalStrategy(new DISecurity());
            return new AirPurifier(networkNode, communicationStrategy);
        }
        return null;
    }
}
