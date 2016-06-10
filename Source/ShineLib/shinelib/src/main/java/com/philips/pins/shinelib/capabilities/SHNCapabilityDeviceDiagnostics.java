/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNMapResultListener;

/**
 * Interface to receive a peripheral specific diagnostic information.
 */
public interface SHNCapabilityDeviceDiagnostics extends SHNCapability {

    /**
     * Returns a map from string to string with plugin specific key value pairs for device diagnostics.
     *
     * @param listener To receive callbacks.
     */
    void readDeviceDiagnostics(@NonNull final SHNMapResultListener<String, String> listener);
}
