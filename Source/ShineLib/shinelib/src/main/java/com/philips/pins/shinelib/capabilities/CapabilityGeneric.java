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
        void onWriteCompleted(final UUID aChar, int status);

        void onReadCompleted(final UUID aChar, byte[] data, int status);

        void onCharacteristicChanged(final UUID aChar, byte[] data, int status);
    }

    void readCharacteristic(SHNDataRawResultListener listener, UUID uuid);

    void writeCharacteristic(SHNDataRawResultListener listener, UUID uuid, byte[] data);

    /**
     * Set callback to receive notifications about battery level changes.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param genericCapabilityListener to receive updates.
     */
    void setCapabilityGenericListener(CapabilityGenericListener genericCapabilityListener);
}
