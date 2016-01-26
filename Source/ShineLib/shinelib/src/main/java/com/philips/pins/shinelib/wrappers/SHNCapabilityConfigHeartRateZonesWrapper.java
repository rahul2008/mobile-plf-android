package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigHeartRateZones;

import java.util.List;

public class SHNCapabilityConfigHeartRateZonesWrapper implements SHNCapabilityConfigHeartRateZones {

    private static final String TAG = SHNCapabilityConfigHeartRateZones.class.getSimpleName();
    private final SHNCapabilityConfigHeartRateZones wrappedShnCapability;
    private final Handler internalHandler;
    private final Handler userHandler;

    public SHNCapabilityConfigHeartRateZonesWrapper(SHNCapabilityConfigHeartRateZones wrappedShnCapability, Handler internalHandler, Handler userHandler) {
        this.wrappedShnCapability = wrappedShnCapability;
        this.userHandler = userHandler;
        this.internalHandler = internalHandler;
    }

    @Override
    public void getSupportedHeartRateZoneThresholdsCount(@NonNull final ResultListener<Integer> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getSupportedHeartRateZoneThresholdsCount(new ResultListener<Integer>() {
                    @Override
                    public void onActionCompleted(final Integer value, @NonNull final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void getHeartRateZoneThresholdsInBpm(@NonNull final ResultListener<List<Integer>> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.getHeartRateZoneThresholdsInBpm(new ResultListener<List<Integer>>() {
                    @Override
                    public void onActionCompleted(final List<Integer> value, @NonNull final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultListener.onActionCompleted(value, result);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void setHeartRateZoneThresholdsInBpm(final List<Integer> heartRateZoneThresholdsInBpm, @NonNull final SHNResultListener resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapability.setHeartRateZoneThresholdsInBpm(heartRateZoneThresholdsInBpm, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        userHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultListener.onActionCompleted(result);
                            }
                        });
                    }
                });
            }
        });
    }
}
