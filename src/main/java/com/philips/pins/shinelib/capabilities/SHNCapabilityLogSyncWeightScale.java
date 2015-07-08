package com.philips.pins.shinelib.capabilities;

import android.util.Log;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale;
import com.philips.pins.shinelib.datatypes.SHNDataWeightMeasurement;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityLogSyncWeightScale extends SHNCapabilityLogSyncBase implements SHNServiceWeightScale.SHNServiceWeightScaleListener {

    private static final String TAG = SHNCapabilityLogSyncWeightScale.class.getSimpleName();

    private SHNServiceWeightScale shnServiceWeightScale;

    public SHNCapabilityLogSyncWeightScale(SHNServiceWeightScale shnServiceWeightScale) {
        super();
        this.shnServiceWeightScale = shnServiceWeightScale;
        shnServiceWeightScale.setShnServiceWeightScaleListener(this);
    }

    @Override
    public void onServiceStateChanged(SHNServiceWeightScale shnServiceWeightScale, SHNService.State state) {
        if (state == SHNService.State.Unavailable || state == SHNService.State.Error) {
            abortSynchronization();
        }
    }

    @Override
    public void onWeightMeasurementReceived(SHNServiceWeightScale shnServiceWeightScale, SHNDataWeightMeasurement shnDataWeightMeasurement) {
        if (getState() == State.Synchronizing) {
            if (shnDataWeightMeasurement.getTimestamp() == null) {
                Log.w(TAG, "The received weight measurement does not have a timestamp, cannot save it in the log!");
                timer.restart();
            } else {
                Map<SHNDataType, SHNData> map = new HashMap<>();
                map.put(SHNDataType.WeightMeasurement, shnDataWeightMeasurement);
                SHNLogItem item = new SHNLogItem(shnDataWeightMeasurement.getTimestamp(), map.keySet(), map);
                onMeasurementReceived(item);
            }
        }
    }

    @Override
    void setupToReceiveMeasurements() {
        shnServiceWeightScale.setNotificationsEnabled(true, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                handleResultOfMeasurementsSetup(result);
            }
        });
    }

    @Override
    void teardownReceivingMeasurements() {
        shnServiceWeightScale.setNotificationsEnabled(false, null);
    }
}

