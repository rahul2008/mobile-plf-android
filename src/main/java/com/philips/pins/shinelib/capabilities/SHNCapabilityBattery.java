package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SHNCapabilityBattery extends SHNCapability {
    public interface SHNBatteryListener{
        void onBatteryLevelChanged(int level);
    }

    void getBatteryLevel(SHNIntegerResultListener listener);
    void setBatteryLevelNotification(boolean enabled, SHNResultListener listener);
}
