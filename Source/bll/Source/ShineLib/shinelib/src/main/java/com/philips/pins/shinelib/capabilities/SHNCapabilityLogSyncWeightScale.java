/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataBodyWeight;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.services.SHNServiceCurrentTime;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale.SHNServiceWeightScaleListener;
import com.philips.pins.shinelib.services.weightscale.SHNWeightMeasurement;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @publicPluginApi
 */
public class SHNCapabilityLogSyncWeightScale extends SHNCapabilityLogSyncBase {
    private static final String TAG = SHNCapabilityLogSyncWeightScale.class.getSimpleName();
    private final SHNServiceWeightScale shnServiceWeightScale;
    private final SHNDeviceTimeAdjuster shnDeviceTimeAdjuster;

    private final SHNServiceWeightScaleListener shnServiceWeightScaleListener = new SHNServiceWeightScaleListener() {
        @Override
        public void onServiceStateChanged(SHNServiceWeightScale shnServiceWeightScale, SHNService.State state) {
            if (state == SHNService.State.Unavailable || state == SHNService.State.Error) {
                abortSynchronization();
            }
        }

        @Override
        public void onWeightMeasurementReceived(SHNServiceWeightScale shnServiceWeightScale, SHNWeightMeasurement shnWeightMeasurement) {
            if (getState() == State.Synchronizing) {
                if (shnWeightMeasurement.getTimestamp() == null) {
                    SHNLogger.w(TAG, "The received weight measurement does not have a timestamp, cannot save it in the log!");
                } else {
                    long hostTimestamp = shnDeviceTimeAdjuster.adjustTimestampToHostTime(shnWeightMeasurement.getTimestamp().getTime());
                    Map<SHNDataType, SHNData> map = new HashMap<>();
                    SHNDataBodyWeight.Builder builder = new SHNDataBodyWeight.Builder().setWeightInKg(shnWeightMeasurement.getWeightInKg());
                    if (shnWeightMeasurement.getFlags().hasUserId()) {
                        builder.setUserId(shnWeightMeasurement.getUserId());
                    }
                    if (shnWeightMeasurement.getFlags().hasBmiAndHeight()) {
                        builder.setHeightInMeters(shnWeightMeasurement.getHeight()).setBmi(shnWeightMeasurement.getBMI());
                    }
                    map.put(SHNDataType.BodyWeight, builder.build());
                    SHNLogItem item = new SHNLogItem(new Date(hostTimestamp), map.keySet(), map);
                    onMeasurementReceived(item);
                }
            }
        }
    };

    private final SHNServiceCurrentTime.SHNServiceCurrentTimeListener shnServiceCurrentTimeListener = new SHNServiceCurrentTime.SHNServiceCurrentTimeListener() {
        @Override
        public void onServiceStateChanged(SHNServiceCurrentTime shnServiceCurrentTime, SHNService.State state) {
            if (state == SHNService.State.Unavailable || state == SHNService.State.Error) {
                abortSynchronization();
            }
        }
    };

    public SHNCapabilityLogSyncWeightScale(SHNServiceWeightScale shnServiceWeightScale, SHNDeviceTimeAdjuster shnDeviceTimeAdjuster) {
        super();
        this.shnServiceWeightScale = shnServiceWeightScale;
        this.shnDeviceTimeAdjuster = shnDeviceTimeAdjuster;
        shnServiceWeightScale.setSHNServiceWeightScaleListener(shnServiceWeightScaleListener);
    }

    @Override
    protected void setupToReceiveMeasurements() {
        shnServiceWeightScale.setNotificationsEnabled(true, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                handleResultOfMeasurementsSetup(result);
            }
        });
    }

    @Override
    protected void teardownReceivingMeasurements() {
        shnServiceWeightScale.setNotificationsEnabled(false, null);
    }
}
