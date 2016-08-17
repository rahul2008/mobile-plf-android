/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNDataRawResultListener;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResultListener;
import java.util.UUID;

public interface CapabilityGeneric extends SHNCapability {

    interface CapabilityGenericListener {

        void onCharacteristicChanged(final UUID aChar, byte[] data, int status);
    }

    void readCharacteristic(SHNDataRawResultListener listener, UUID uuid);

    void writeCharacteristic(SHNResultListener listener, UUID uuid, byte[] data);

    void setNotify(SHNResultListener listener, boolean notify, UUID uuid);

    /**
     * Set callback to receive notifications about battery level changes.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param genericCapabilityListener to receive updates.
     */
    void setCapabilityGenericListener(CapabilityGenericListener genericCapabilityListener);
}
