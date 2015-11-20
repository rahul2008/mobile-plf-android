/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.support.annotation.NonNull;

import java.util.Map;

public interface SHNMapResultListener<K, V> {
    void onActionCompleted(@NonNull final Map<K, V> value, @NonNull final SHNResult result);
}
