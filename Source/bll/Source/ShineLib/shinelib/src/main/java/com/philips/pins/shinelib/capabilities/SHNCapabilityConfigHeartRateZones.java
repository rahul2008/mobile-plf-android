package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;

import java.util.List;

public interface SHNCapabilityConfigHeartRateZones extends SHNCapability {

    void getSupportedHeartRateZoneThresholdsCount(@NonNull final ResultListener<Integer> resultListener);

    void getHeartRateZoneThresholdsInBpm(@NonNull final  ResultListener<List<Integer>> resultListener);

    void setHeartRateZoneThresholdsInBpm(List<Integer> heartRateZoneThresholdsInBpm, @NonNull SHNResultListener resultListener);
}
