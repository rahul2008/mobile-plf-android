/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface SHNCommandResultReporter {
    void reportResult(@NonNull final SHNResult shnResult, @Nullable final byte[] data);
}
