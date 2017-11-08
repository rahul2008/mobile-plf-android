/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivity.appliance;


import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.platform.baseapp.screens.utility.RALog;

public class RefAppBleReferenceAppliance extends Appliance {

    public static final String MODELNAME = "ReferenceNode";
    public static final String TAG = "RefAppBleReferenceAppliance";

    private final DeviceMeasurementPort deviceMeasurementPort;

    public RefAppBleReferenceAppliance(NetworkNode networkNode, CommunicationStrategy communicationStrategy) {
        super(networkNode, communicationStrategy);
        deviceMeasurementPort=new DeviceMeasurementPort(communicationStrategy);
        addPort(deviceMeasurementPort);
        RALog.d(TAG,"Adding device Measurement port to appliance");

    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }


    public DeviceMeasurementPort getDeviceMeasurementPort(){
        return deviceMeasurementPort;
    }
}
