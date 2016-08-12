/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNDataRawResultListener;
import java.util.UUID;

public interface CapabilityGeneric extends SHNCapability {

    interface CapabilityGenericListener {
        void onReadCompleted(final UUID aChar, byte[] data);

        // TODO: onWrite

        // TODO: notify
    }

    void readCharacteristic(SHNDataRawResultListener listener, UUID uuid);

    /**
     * Set callback to receive notifications about battery level changes.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param genericCapabilityListener to receive updates.
     */
    void setCapabilityGenericListener(CapabilityGenericListener genericCapabilityListener);
}
