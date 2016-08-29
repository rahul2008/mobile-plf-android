/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;
import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import java.util.UUID;

public interface CapabilityBluetoothDirect extends SHNCapability {

    interface CharacteristicChangedListener {

        void onCharacteristicChanged(@NonNull final UUID characteristic, byte[] data, int status);
    }

    void readCharacteristic(@NonNull ResultListener<SHNDataRaw> listener, @NonNull UUID uuid);

    void writeCharacteristic(@NonNull SHNResultListener listener, @NonNull UUID uuid, byte[] data);

    void setNotifyOnCharacteristicChange(@NonNull SHNResultListener listener, @NonNull UUID uuid, boolean notify);

    /**
     * Set callback to receive notifications about changes in characteristic.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param listener to receive updates.
     */
    void setCharacteristicChangedListener(@NonNull CharacteristicChangedListener listener);
}
