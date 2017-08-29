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
import com.philips.platform.appframework.connectivitypowersleep.ConfigPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.DeviceDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.GenericPort;
import com.philips.platform.appframework.connectivitypowersleep.HypnogramDataPort;
import com.philips.platform.appframework.connectivitypowersleep.HypnogramDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.SensorDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.SessionDataPort;
import com.philips.platform.appframework.connectivitypowersleep.SessionDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.SessionInfoPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.TimePortProperties;
import com.philips.platform.baseapp.screens.utility.RALog;

public class BleReferenceAppliance extends Appliance {

    public static final String MODELNAME = "ReferenceNode";
    public static final String TAG = "BleReferenceAppliance";

    private DeviceMeasurementPort deviceMeasurementPort;
    public static final String MODEL_NAME_1 = "HH1600";

    private static final int PRODUCT_ID = 1;

    @NonNull
    private GenericPort<ConfigPortProperties> powerSleepConfigPort;

    @NonNull
    private GenericPort<DeviceDataPortProperties> powerSleepDevicePort;
    @NonNull
    private GenericPort<SensorDataPortProperties> powerSleepSensorPort;
    @NonNull
    private SessionDataPort powerSleepSessionDataPort;
    @NonNull
    private HypnogramDataPort powerSleepHypnogramPort;
    @NonNull
    private GenericPort<SessionInfoPortProperties> powerSleepSessionInfoPort;
    @NonNull
    private GenericPort<TimePortProperties> powerSleepTimePort;

    public BleReferenceAppliance(@NonNull NetworkNode networkNode, @NonNull CommunicationStrategy communicationStrategy, ConnectivityDeviceType deviceType) {
        super(networkNode, communicationStrategy);

        initializePorts(deviceType, communicationStrategy);

    }

    private void initializePorts(ConnectivityDeviceType deviceType, CommunicationStrategy communicationStrategy) {
        switch (deviceType) {
            case POWER_SLEEP:
                powerSleepDevicePort = new GenericPort<>(communicationStrategy, "device", PRODUCT_ID, DeviceDataPortProperties.class);
                powerSleepSessionInfoPort = new GenericPort<>(communicationStrategy, "session", PRODUCT_ID, SessionInfoPortProperties.class);
                powerSleepSessionDataPort = new SessionDataPort(communicationStrategy, "session", PRODUCT_ID, SessionDataPortProperties.class);

                powerSleepTimePort = new GenericPort<>(communicationStrategy, "time", PRODUCT_ID, TimePortProperties.class);
                powerSleepSensorPort = new GenericPort<>(communicationStrategy, "sensor", PRODUCT_ID, SensorDataPortProperties.class);
                powerSleepConfigPort = new GenericPort<>(communicationStrategy, "config", PRODUCT_ID, ConfigPortProperties.class);
                powerSleepHypnogramPort = new HypnogramDataPort(communicationStrategy, "session/%s/hypnogram", PRODUCT_ID, HypnogramDataPortProperties.class);

                addPort(powerSleepDevicePort);
                addPort(powerSleepSessionInfoPort);
                addPort(powerSleepSessionDataPort);
                addPort(powerSleepTimePort);
                addPort(powerSleepSensorPort);
                addPort(powerSleepHypnogramPort);
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


    public DeviceMeasurementPort getDeviceMeasurementPort() {
        return deviceMeasurementPort;
    }

    @NonNull
    public GenericPort<DeviceDataPortProperties> getDeviceDataPort() {
        return powerSleepDevicePort;
    }

    @NonNull
    public GenericPort<SessionInfoPortProperties> getSessionInfoPort() {
        return powerSleepSessionInfoPort;
    }

    @NonNull
    public GenericPort<TimePortProperties> getPowerSleepTimePort() {
        return powerSleepTimePort;
    }

    @NonNull
    public SessionDataPort getSessionDataPort() {
        return powerSleepSessionDataPort;
    }

    @NonNull
    public GenericPort<SensorDataPortProperties> getSensorDataPort() {
        return powerSleepSensorPort;
    }

    @NonNull
    public HypnogramDataPort getHypnogramDataPort() {
        return powerSleepHypnogramPort;
    }

    @NonNull
    public GenericPort<ConfigPortProperties> getPowerSleepConfigPort() {
        return powerSleepConfigPort;
    }


}
