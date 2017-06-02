/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.services.SHNServiceBattery;

/**
 * @publicPluginApi
 */
public class SHNCapabilityBatteryImpl implements SHNCapabilityBattery {

    private final SHNServiceBattery shnServiceBattery;

    private SHNCapabilityBatteryListener shnCapabilityBatteryListener;

    public SHNCapabilityBatteryImpl(SHNServiceBattery shnServiceBattery) {
        this.shnServiceBattery = shnServiceBattery;
        this.shnServiceBattery.setShnServiceBatteryListener(shnServiceBatteryListener);
    }

    private SHNServiceBattery.SHNServiceBatteryListener shnServiceBatteryListener = new SHNServiceBattery.SHNServiceBatteryListener() {
        @Override
        public void onBatteryLevelUpdated(int level) {
            if (shnCapabilityBatteryListener != null) {
                shnCapabilityBatteryListener.onBatteryLevelChanged(level);
            }
        }
    };

    // implements SHNCapabilityBattery
    @Override
    public void getBatteryLevel(SHNIntegerResultListener listener) {
        shnServiceBattery.getBatteryLevel(listener);
    }

    @Override
    public void setBatteryLevelNotifications(boolean enabled, SHNResultListener listener) {
        shnServiceBattery.setBatteryLevelNotifications(enabled, listener);
    }

    @Override
    public void setSetSHNCapabilityBatteryListener(SHNCapabilityBatteryListener shnCapabilityBatteryListener) {
        this.shnCapabilityBatteryListener = shnCapabilityBatteryListener;
    }
}
