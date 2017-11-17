/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivity.appliance;


import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPortProperties;
import com.philips.platform.baseapp.screens.utility.RALog;

public class RefAppBleReferenceAppliance extends Appliance {

    public static final String MODELNAME = "ReferenceNode";
    public static final String TAG = "RefAppBleReferenceAppliance";

    private DeviceMeasurementPort deviceMeasurementPort;
    public static final String MODEL_NAME_HH1600 = "HH1600";
    public static final String MODEL_NAME_HHS = "HHS";

    private static final int PRODUCT_ID = 1;
    private CommunicationStrategy communicationStrategy;

    @NonNull
    private SessionDataPort powerSleepSessionDataPort;

    public RefAppBleReferenceAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy, ConnectivityDeviceType deviceType) {
        super(networkNode, communicationStrategy);
        this.communicationStrategy = communicationStrategy;
 		initializePorts(deviceType, communicationStrategy);
    }

    private void initializePorts(ConnectivityDeviceType deviceType, CommunicationStrategy communicationStrategy) {
        switch (deviceType) {
            case POWER_SLEEP:
                powerSleepSessionDataPort = new SessionDataPort(communicationStrategy, "session", PRODUCT_ID, SessionDataPortProperties.class);
                addPort(powerSleepSessionDataPort);
                break;

            case REFERENCE_NODE:
                deviceMeasurementPort = new DeviceMeasurementPort(communicationStrategy);
                addPort(deviceMeasurementPort);
                RALog.d(TAG, "Adding device Measurement port to appliance");
                break;
        }
    }

    @Override
    public String getDeviceType() {
        return MODELNAME;
    }


    public DeviceMeasurementPort getDeviceMeasurementPort(){
        return deviceMeasurementPort;
    }

    @NonNull
    public SessionDataPort getSessionDataPort() {
        return powerSleepSessionDataPort;
    }
}
