/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.appframework.connectivity.appliance;


import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class BleReferenceAppliance extends Appliance {

    public static final String MODELNAME = "ReferenceNode";
    private final DeviceMeasurementPort deviceMeasurementPort;

    public BleReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
        deviceMeasurementPort=new DeviceMeasurementPort(communicationStrategy);
        addPort(deviceMeasurementPort);

    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }


    public DeviceMeasurementPort getDeviceMeasurementPort(){
        return deviceMeasurementPort;
    }
}
