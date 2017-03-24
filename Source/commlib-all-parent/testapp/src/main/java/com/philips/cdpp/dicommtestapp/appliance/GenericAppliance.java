package com.philips.cdpp.dicommtestapp.appliance;

import android.util.Log;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdpp.dicommtestapp.appliance.property.ApplianceSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.PortSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.Property;

import java.util.ArrayList;
import java.util.List;


public class GenericAppliance extends Appliance
{
    private static final String TAG = "GenericAppliance";
    private String modelNumber;
    private String deviceName;

    GenericAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
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

    public List<PropertyPort> getPropertyPorts()
    {
        List<PropertyPort> propPorts = new ArrayList<>();
        for(DICommPort port : getAllPorts()) {
            if(port instanceof PropertyPort) {
                propPorts.add((PropertyPort) port);
            }
        }
        return propPorts;
    }

    void readApplianceSpecification(ApplianceSpecification applianceSpec, CommunicationStrategy strategy) {
        this.addSpecificationPorts(applianceSpec, strategy);
        this.modelNumber = applianceSpec.getModelNumber();
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
