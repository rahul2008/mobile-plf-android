/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer.appliance;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdp2.commlibexplorer.SupportedAppliances;
import com.philips.cdp2.commlibexplorer.appliance.property.ApplianceSpecification;

import java.util.HashSet;
import java.util.Set;

public class GenericApplianceFactory implements DICommApplianceFactory<GenericAppliance> {
    private TransportContext transportContext;
    private SupportedAppliances supportedAppliances;

    public GenericApplianceFactory() {
        supportedAppliances = new SupportedAppliances();
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return networkNode != null;
    }

    @Override
    public GenericAppliance createApplianceForNode(NetworkNode networkNode) {
        CommunicationStrategy strategy = transportContext.createCommunicationStrategyFor(networkNode);
        ApplianceSpecification applianceSpec = supportedAppliances.findSpecification(networkNode.getModelId(), networkNode.getDeviceType());

        GenericAppliance genericAppliance = new GenericAppliance(networkNode, strategy);
        genericAppliance.readApplianceSpecification(applianceSpec, strategy);

        return genericAppliance;
    }

    @Override
    public Set<String> getSupportedDeviceTypes() {
        Set<String> modelNames = new HashSet<>();
        modelNames.add("AirPurifier");
        modelNames.add("Polaris");
        modelNames.add("ReferenceNode");
        modelNames.add("uGrowSmartBabyMonitor");
        modelNames.add("DiProduct");
        return modelNames;
    }

    public void setTransportContext(TransportContext transportContext) {
        this.transportContext = transportContext;
    }
}
