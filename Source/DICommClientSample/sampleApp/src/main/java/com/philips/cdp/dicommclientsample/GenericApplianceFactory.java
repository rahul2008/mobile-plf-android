package com.philips.cdp.dicommclientsample;

import android.util.Log;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.LocalStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
class GenericApplianceFactory extends DICommApplianceFactory<GenericAppliance> {

    public static final String MODELNAME_AIRPURIFIER = "AirPurifier";

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        if (networkNode.getModelName().equals(MODELNAME_AIRPURIFIER)) {
            // Optionally add a check for the modeltype and return a different
            // DICommAppliance depending on the type.
            return true;
        }
        return false;
    }

    @Override
    public GenericAppliance createApplianceForNode(NetworkNode networkNode) {
        if (networkNode.getModelName().equals(MODELNAME_AIRPURIFIER)) {
            LocalStrategy communicationStrategy = new LocalStrategy(new DISecurity());
            return new GenericAppliance(networkNode, communicationStrategy);
        }
        return null;
    }
}
