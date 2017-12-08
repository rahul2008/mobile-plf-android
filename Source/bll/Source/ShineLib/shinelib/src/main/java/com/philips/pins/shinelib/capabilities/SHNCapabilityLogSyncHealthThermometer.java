/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;



import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataBodyTemperature;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.services.healththermometer.SHNServiceHealthThermometer;
import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurement;
import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurementInterval;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @publicPluginApi
 */
public class SHNCapabilityLogSyncHealthThermometer extends SHNCapabilityLogSyncBase implements SHNServiceHealthThermometer.SHNServiceHealthThermometerListener {

    private static final String TAG = SHNCapabilityLogSyncHealthThermometer.class.getSimpleName();

    private final SHNServiceHealthThermometer shnServiceHealthThermometer;
    private final SHNDeviceTimeAdjuster shnDeviceTimeAdjuster;

    public SHNCapabilityLogSyncHealthThermometer(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNDeviceTimeAdjuster shnDeviceTimeAdjuster) {
        this.shnServiceHealthThermometer = shnServiceHealthThermometer;
        this.shnDeviceTimeAdjuster = shnDeviceTimeAdjuster;
        shnServiceHealthThermometer.setSHNServiceHealthThermometerListener(this);
    }

    @Override
    public void onServiceStateChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNService.State state) {
        if (state == SHNService.State.Unavailable || state == SHNService.State.Error) {
            abortSynchronization();
        }
    }

    @Override
    public void onTemperatureMeasurementReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement) {
        if (getState() == State.Synchronizing) {
            if (shnTemperatureMeasurement.getTimestamp() == null) {
                SHNLogger.w(TAG, "The received temperature measurement does not have a timestamp, cannot save it in the log!");
            } else {
                long hostTimestamp = shnDeviceTimeAdjuster.adjustTimestampToHostTime(shnTemperatureMeasurement.getTimestamp().getTime());
                Map<SHNDataType, SHNData> map = new HashMap<>();
                map.put(SHNDataType.BodyTemperature, new SHNDataBodyTemperature(shnTemperatureMeasurement.getTemperatureInCelcius(), shnTemperatureMeasurement.getSHNTemperatureType()));
                SHNLogItem item = new SHNLogItem(new Date(hostTimestamp), map.keySet(), map);
                onMeasurementReceived(item);
            }
        }
    }

    @Override
    public void onIntermediateTemperatureReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement) {
    }

    @Override
    public void onMeasurementIntervalChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval) {
    }

    @Override
    protected void setupToReceiveMeasurements() {
        shnServiceHealthThermometer.setReceiveTemperatureMeasurements(true, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                handleResultOfMeasurementsSetup(result);
            }
        });
    }

    @Override
    protected void teardownReceivingMeasurements() {
        shnServiceHealthThermometer.setReceiveTemperatureMeasurements(false, null);
    }
}
