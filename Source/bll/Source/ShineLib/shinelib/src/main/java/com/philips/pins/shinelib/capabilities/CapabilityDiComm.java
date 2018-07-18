/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.datatypes.SHNStreamData;
import com.philips.pins.shinelib.datatypes.StreamIdentifier;

/**
 * Capability to be used by CommLib to enable DiComm communication.
 */
public interface CapabilityDiComm extends SHNCapability {
    /**
     * Write diComm message data
     *
     * @param data to write
     */
    void writeData(@NonNull byte[] data, StreamIdentifier streamIdentifier);

    /**
     * Attach a callback for data received from the peripheral
     *
     * @param dataRawResultListener to receive callback data
     */
    void addDataListener(@NonNull ResultListener<SHNStreamData> dataRawResultListener);

    /**
     * Remove a callback for data received from the peripheral
     *
     * @param dataRawResultListener to be removed
     */
    void removeDataListener(@NonNull ResultListener<SHNStreamData> dataRawResultListener);
}
