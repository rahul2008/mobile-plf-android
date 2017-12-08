/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNCapability;

public interface SHNCapabilityConfigSedentary extends SHNCapability {
    void setSedentaryPeriodInMinutes(final short minutes, @NonNull final ResultListener<Short> shnResultListener);

    void getSedentaryPeriodInMinutes(@NonNull final ResultListener<Short> resultListener);

    void setSedentaryNotificationEnabled(final boolean enabled, @NonNull final ResultListener<Boolean> shnResultListener);

    void getSedentaryNotificationEnabled(@NonNull final ResultListener<Boolean> shnResultListener);
}
