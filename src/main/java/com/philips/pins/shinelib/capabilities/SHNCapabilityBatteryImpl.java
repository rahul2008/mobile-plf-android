package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.services.SHNServiceBattery;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNCapabilityBatteryImpl implements SHNCapabilityBattery{

    private final SHNServiceBattery shnServiceBattery;

    public SHNCapabilityBatteryImpl(SHNServiceBattery shnServiceBattery) {
        this.shnServiceBattery = shnServiceBattery;
    }

    // implements SHNCapabilityBattery
    @Override
    public void getBatteryLevel(SHNIntegerResultListener listener) {
        shnServiceBattery.getBatteryLevel(listener);
    }

    @Override
    public void setBatteryLevelNotification(boolean enabled, SHNResultListener listener) {
        shnServiceBattery.setBatteryLevelNotification(enabled, listener);
    }
}
