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
     * Interface to receive notifications for battery level changes.
     */
    interface SHNCapabilityBatteryListener {
        /**
         * Indicates that the battery level has changed.
         *
         * @param level new battery level in percent
         */
        void onBatteryLevelChanged(int level);
    }

    /**
     * Get battery level asynchronously.
     *
     * @param listener will be called when battery level has been retrieved
     */
    void getBatteryLevel(SHNIntegerResultListener listener);

    /**
     * Enable or disable battery notifications. Note that notifications are received via
     * {@code SHNCapabilityBatteryListener} which can be set via {@link #setSetSHNCapabilityBatteryListener(SHNCapabilityBatteryListener)}.
     *
     * @param enabled  flag to enable or disable notifications
     * @param listener callback to provide feedback about subscription result
     */
    void setBatteryLevelNotifications(boolean enabled, SHNResultListener listener);

    /**
     * Set callback to receive notifications about battery level changes.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param shnCapabilityBatteryListener to receive updates.
     */
    void setSetSHNCapabilityBatteryListener(SHNCapabilityBatteryListener shnCapabilityBatteryListener);
}
