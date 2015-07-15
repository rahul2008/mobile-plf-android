package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SHNCapabilityBattery extends SHNCapability {
    interface SHNCapabilityBatteryListener {
        void onBatteryLevelChanged(int level);
    }

    void getBatteryLevel(SHNIntegerResultListener listener);
    void setBatteryLevelNotifications(boolean enabled, SHNResultListener listener);
    void setSetSHNCapabilityBatteryListener(SHNCapabilityBatteryListener shnCapabilityBatteryListener);
}
