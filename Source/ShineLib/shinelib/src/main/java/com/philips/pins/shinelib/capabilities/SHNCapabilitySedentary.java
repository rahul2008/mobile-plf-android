/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;

public interface SHNCapabilitySedentary extends SHNCapability {
    void setSedentaryPeriodInMinutes(@IntRange(from = 0, to = 255) final short minutes, @NonNull final ResultListener<Integer> shnResultListener);

    void getSedentaryPeriodInMinutes(@NonNull final ResultListener<Integer> resultListener);

    void setSedentaryNotificationEnabled(final boolean enabled, @NonNull final ResultListener<Boolean> shnResultListener);

    void getSedentaryNotificationEnabled(@NonNull final ResultListener<Boolean> shnResultListener);
}
