package com.philips.pins.shinelib.capabilities;

import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.services.healththermometer.SHNServiceHealthThermometer;
import com.philips.pins.shinelib.datatypes.SHNDataTemperatureMeasurement;
import com.philips.pins.shinelib.services.healththermometer.SHNTemperatureMeasurementInterval;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310188215 on 17/06/15.
 */
public class SHNCapabilityHealthThermometerLogSync extends SHNCapabilityLogSyncBase implements SHNServiceHealthThermometer.SHNServiceHealthThermometerListener {

    private static final String TAG = SHNCapabilityHealthThermometerLogSync.class.getSimpleName();

    private SHNServiceHealthThermometer shnServiceHealthThermometer;

    public SHNCapabilityHealthThermometerLogSync(SHNServiceHealthThermometer shnServiceHealthThermometer) {
        super();
        this.shnServiceHealthThermometer = shnServiceHealthThermometer;
        shnServiceHealthThermometer.setSHNServiceHealthThermometerListener(this);
    }

    @Override
    public void onServiceStateChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNService.State state) {
        if (state == SHNService.State.Unavailable || state == SHNService.State.Error) {
            abortSynchronization();
        }
    }

    // implements SHNServiceHealthThermometer.SHNServiceHealthThermometerListener
    @Override
    public void onTemperatureMeasurementReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNDataTemperatureMeasurement shnDataTemperatureMeasurement) {
        if (getState() == State.Synchronizing) {
            if (shnDataTemperatureMeasurement.getTimestamp() == null) {
                Log.w(TAG, "The received temperature measurement does not have a timestamp, cannot save it in the log!");
                timer.restart();
            } else {
                Map<SHNDataType, SHNData> map = new HashMap<>();
                map.put(SHNDataType.BodyTemperature, shnDataTemperatureMeasurement);
                SHNLogItem item = new SHNLogItem(shnDataTemperatureMeasurement.getTimestamp(), map.keySet(), map);
                onMeasurementReceived(item);
            }
        }
    }

    @Override
    public void onIntermediateTemperatureReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNDataTemperatureMeasurement shnDataTemperatureMeasurement) {

    }

    @Override
    public void onMeasurementIntervalChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval) {

    }

    @Override
    public void setupToReceiveMeasurements() {
        shnServiceHealthThermometer.setReceiveTemperatureMeasurements(true, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                handleResultOfMeasurementsSetup(result);
            }
        });
    }

    @Override
    void teardownReceivingMeasurements() {
        shnServiceHealthThermometer.setReceiveTemperatureMeasurements(false, null);
    }
}
