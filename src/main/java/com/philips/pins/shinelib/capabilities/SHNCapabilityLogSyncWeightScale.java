package com.philips.pins.shinelib.capabilities;

/*
@startuml
actor UserApp
participant WSLSCapability
participant WSService
participant DevTimeAdjusterCTS
participant CTService
participant SHNService
UserApp -> WSLSCapability : getState() = Idle
SHNService -> CTService : onStateChanged(Available)
CTService -> DevTimeAdjusterCTS: onStateChanged(Available)
DevTimeAdjusterCTS -> CTService : getCurrentTime
CTService -> DevTimeAdjusterCTS : getCurrentTimeResultOK
DevTimeAdjusterCTS -> DevTimeAdjusterCTS : getCurrentSystemTime
DevTimeAdjusterCTS -> CTService : transitionToReady
CTService -> SHNService : transitionToReady
UserApp -> WSLSCapability : getState() = Idle
UserApp -> WSLSCapability : startSynchronizationFromToken(null)
UserApp -> WSLSCapability : getState() = Synchronizing
WSLSCapability -> WSService : enableNotifications
loop all stored measurements
    WSLSCapability -> WSLSCapability : restartTimer
    WSService -> DevTimeAdjusterCTS : adjustTimeToHostTime
    WSService -> WSLSCapability : Measurement
end
WSLSCapability -> WSLSCapability : timeout
WSLSCapability -> WSService : disableNotifications
WSLSCapability -> UserApp : onLogSynchronized
@enduml
 */

import android.util.Log;

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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityLogSyncWeightScale extends SHNCapabilityLogSyncBase {
    private static final String TAG = SHNCapabilityLogSyncWeightScale.class.getSimpleName();
    private static final boolean LOGGING = false;
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
                    Log.w(TAG, "The received weight measurement does not have a timestamp, cannot save it in the log!");
                    timer.restart();
                } else {
                    long hostTimestamp = shnDeviceTimeAdjuster.adjustTimestampToHostTime(shnWeightMeasurement.getTimestamp().getTime());
                    Map<SHNDataType, SHNData> map = new HashMap<>();
                    SHNDataBodyWeight.Builder builder = new SHNDataBodyWeight.Builder().setWeightInKg(shnWeightMeasurement.getWeightInKg());
                    if(shnWeightMeasurement.getFlags().hasUserId()){
                        builder.setUserId(shnWeightMeasurement.getUserId());
                    }
                    if(shnWeightMeasurement.getFlags().hasBmiAndHeight()){
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
