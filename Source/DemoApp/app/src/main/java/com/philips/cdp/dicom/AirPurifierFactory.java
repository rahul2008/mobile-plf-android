package com.philips.cdp.dicom;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.CommunicationMarshal;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AirPurifierFactory extends DICommApplianceFactory<AirPurifier> {

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return networkNode.getModelName().equals(AirPurifier.MODELNAME);
    }

    @Override
    public AirPurifier createApplianceForNode(NetworkNode networkNode) {
        if (networkNode.getModelName().equals(AirPurifier.MODELNAME)) {
            CommunicationStrategy communicationStrategy = new CommunicationMarshal(new DISecurity());
            return new AirPurifier(networkNode, communicationStrategy);
        }
        return null;
    }
}
