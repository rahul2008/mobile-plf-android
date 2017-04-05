/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdpp.dicommtestapp.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdpp.dicommtestapp.appliance.property.ApplianceSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.PortSpecification;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GenericAppliance extends Appliance
{
    private String modelNumber;
    private String deviceName;

    GenericAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
        getAllPorts().clear();
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String getDeviceType() {
        // TODO Ask ConArtists what is the purpose of this method?
        return "";
    }

    public Set<PropertyPort> getPropertyPorts()
    {
        Set<PropertyPort> propPorts = new CopyOnWriteArraySet<>();
        for(DICommPort port : getAllPorts()) {
            if(port instanceof PropertyPort) {
                propPorts.add((PropertyPort) port);
            }
        }
        return propPorts;
    }

    void readApplianceSpecification(ApplianceSpecification applianceSpec, CommunicationStrategy strategy) {
        this.addSpecificationPorts(applianceSpec, strategy);
        this.modelNumber = applianceSpec.getModelId();
        this.deviceName = applianceSpec.getDeviceName();
    }

    private void addSpecificationPorts(ApplianceSpecification applianceSpec, CommunicationStrategy strategy) {
        for(PortSpecification portSpec : applianceSpec.getPortSpecifications())
        {
            PropertyPort propPort = new PropertyPort(strategy, portSpec);
            addPort(propPort);
        }
    }
}
