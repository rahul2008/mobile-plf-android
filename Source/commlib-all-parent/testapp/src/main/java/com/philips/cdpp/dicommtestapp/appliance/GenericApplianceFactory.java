package com.philips.cdpp.dicommtestapp.appliance;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.context.TransportContext;
import com.philips.cdpp.dicommtestapp.SupportedAppliances;
import com.philips.cdpp.dicommtestapp.appliance.property.ApplianceSpecification;

import java.util.HashSet;
import java.util.Set;

public class GenericApplianceFactory implements DICommApplianceFactory<GenericAppliance>
{
    private static final String TAG = "GenericApplianceFactory";
    private TransportContext mTransportContext;
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
        // TODO Refactor this when libraries have been unified (BLE devices now have 'null' for modelType)
        if(networkNode.getModelId() == null) {
            networkNode.setModelId("unknown");
        }

        CommunicationStrategy strategy = mTransportContext.createCommunicationStrategyFor(networkNode);
        ApplianceSpecification applianceSpec = supportedAppliances.findSpecification(networkNode.getModelId());

        GenericAppliance genericAppliance = new GenericAppliance(networkNode, strategy);
        genericAppliance.readApplianceSpecification(applianceSpec, strategy);

        return genericAppliance;
    }

    @Override
    public Set<String> getSupportedModelNames() {
        HashSet<String> modelNames = new HashSet<>();
        modelNames.add("AirPurifier");
        modelNames.add("Polaris");
        modelNames.add("ReferenceNode");
        modelNames.add("uGrowSmartBabyMonitor");
        modelNames.add("DiProduct");
        return modelNames;
    }

    public void setTransportContext(TransportContext transportContext) {
        mTransportContext = transportContext;
    }
}
