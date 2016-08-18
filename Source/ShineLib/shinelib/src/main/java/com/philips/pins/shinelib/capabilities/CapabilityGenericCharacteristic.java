/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNDataRawResultListener;
import com.philips.pins.shinelib.SHNResultListener;
import java.util.UUID;

public interface CapabilityGenericCharacteristic extends SHNCapability {

    interface CharacteristicChangedListener {

        void onCharacteristicChanged(@NonNull final UUID aChar, byte[] data, int status);
    }

    void readCharacteristic(@NonNull SHNDataRawResultListener listener, @NonNull UUID uuid);

    void writeCharacteristic(@NonNull SHNResultListener listener, @NonNull UUID uuid, byte[] data);

    void setNotify(@NonNull SHNResultListener listener, @NonNull UUID uuid, boolean notify);

    /**
     * Set callback to receive notifications about battery level changes.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param listener to receive updates.
     */
    void setCharacteristicChangedListener(@NonNull CharacteristicChangedListener listener);
}
