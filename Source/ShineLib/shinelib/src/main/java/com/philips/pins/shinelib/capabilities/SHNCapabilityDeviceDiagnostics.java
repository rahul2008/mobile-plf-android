/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNMapResultListener;

public interface SHNCapabilityDeviceDiagnostics extends SHNCapability {

    /**
     * Will return a map from string to string with plugin specific key value pairs for device diagnostics.
     * All keys are prefixed with the device name, this guarantees uniqueness of the keys, even out of the scope
     * of the current product.
     *
     * @see <a href="https://wiki.research.philips.com/foswiki/bin/view/Shine/WebHome">The Shine wiki</a>
     *
     * @param listener To receive callbacks.
     */
    void readDeviceDiagnostics(@NonNull final SHNMapResultListener<String, String> listener);
}
