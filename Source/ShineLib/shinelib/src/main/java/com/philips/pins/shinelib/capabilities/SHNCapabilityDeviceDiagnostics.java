/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNMapResultListener;

public interface SHNCapabilityDeviceDiagnostics extends SHNCapability {

    void readDeviceDiagnostics(@NonNull final SHNMapResultListener<String, String> listener);
}
