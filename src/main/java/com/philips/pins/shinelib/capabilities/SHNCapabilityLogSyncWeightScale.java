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

import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;
import com.philips.pins.shinelib.datatypes.SHNLogItem;
import com.philips.pins.shinelib.services.SHNServiceCurrentTime;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale;
import com.philips.pins.shinelib.datatypes.SHNDataWeightMeasurement;
import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale.SHNServiceWeightScaleListener;

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

    private final SHNServiceWeightScaleListener shnServiceWeightScaleListener= new SHNServiceWeightScaleListener() {
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
                    long hostTimestamp = shnDeviceTimeAdjuster.adjustTimestampToHostTime(shnDataWeightMeasurement.getTimestamp().getTime());
                    shnDataWeightMeasurement.getTimestamp().setTime(hostTimestamp);
                    Map<SHNDataType, SHNData> map = new HashMap<>();
                    map.put(SHNDataType.WeightMeasurement, shnDataWeightMeasurement);
                    SHNLogItem item = new SHNLogItem(shnDataWeightMeasurement.getTimestamp(), map.keySet(), map);
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
    private final SHNDeviceTimeAdjuster shnDeviceTimeAdjuster;

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

    private void handleGetCurrentTimeCompletion(Object object, SHNResult result) {
        shnServiceWeightScale.setNotificationsEnabled(true, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                handleResultOfMeasurementsSetup(SHNResult.SHNOk);
                timer.restart();
            }
        });
    }

    @Override
    void teardownReceivingMeasurements() {
        shnServiceWeightScale.setNotificationsEnabled(false, null);
    }
}
