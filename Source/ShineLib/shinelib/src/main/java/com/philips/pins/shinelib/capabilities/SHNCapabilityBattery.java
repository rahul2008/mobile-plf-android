/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;

/**
 * Interface to get battery level from a peripheral and subscribe for battery level change notifications.
 */
public interface SHNCapabilityBattery extends SHNCapability {

    /**
     * Interface to receive notification for battery level change.
     */
    interface SHNCapabilityBatteryListener {
        /**
         * Indicates that battery level has changed
         *
         * @param level new battery level
         */
        void onBatteryLevelChanged(int level);
    }

    /**
     * Get battery level.
     *
     * @param listener callback for battery level
     */
    void getBatteryLevel(SHNIntegerResultListener listener);

    /**
     * Enable or disable battery notifications. Note that notification are received via {@code SHNCapabilityBatteryListener} which can be set via the setter method.
     *
     * @param enabled  flag to enable or disable notifications.
     * @param listener callback to provide feedback about subscription result.
     */
    void setBatteryLevelNotifications(boolean enabled, SHNResultListener listener);

    /**
     * Set callback to receive notification about battery level changed. Note requires subscription t be enabled.
     *
     * @param shnCapabilityBatteryListener to receive updates.
     */
    void setSetSHNCapabilityBatteryListener(SHNCapabilityBatteryListener shnCapabilityBatteryListener);
}
