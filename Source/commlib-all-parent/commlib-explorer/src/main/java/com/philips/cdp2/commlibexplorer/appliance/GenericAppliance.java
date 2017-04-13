/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlibexplorer.SupportedPorts;
import com.philips.cdp2.commlibexplorer.appliance.property.ApplianceSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.PortSpecification;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GenericAppliance extends Appliance {
    private String modelId;
    private String deviceName;
    private SupportedPorts supportedPorts = new SupportedPorts();

    GenericAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
        getAllPorts().clear();
        addPort(mPairingPort);
    }

    public String getModelId() {
        return modelId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getDeviceType() {
        return "";
    }

    public Set<SupportedPort> getSupportedPorts() {
        Set<SupportedPort> supportedPorts = new CopyOnWriteArraySet<>();
        for (DICommPort port : getAllPorts()) {
            if (port instanceof SupportedPort) {
                supportedPorts.add((SupportedPort) port);
            } else if (this.supportedPorts.getSupportedPorts().contains(port.getClass())) {
                supportedPorts.add(new NativePort(port));
            }
        }
        return supportedPorts;
    }

    void readApplianceSpecification(ApplianceSpecification applianceSpec, CommunicationStrategy strategy) {
        this.addSpecificationPorts(applianceSpec, strategy);
        this.modelId = applianceSpec.getModelId();
        this.deviceName = applianceSpec.getDeviceName();
    }

    private void addSpecificationPorts(ApplianceSpecification applianceSpec, CommunicationStrategy strategy) {
        for (PortSpecification portSpec : applianceSpec.getPortSpecifications()) {
            PropertyPort propPort = new PropertyPort(strategy, portSpec);
            addPort(propPort);
        }
    }
}
