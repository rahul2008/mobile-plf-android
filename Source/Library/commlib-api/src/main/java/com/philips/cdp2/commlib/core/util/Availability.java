/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

public interface Availability<T extends Availability> {

    boolean isAvailable();

    void addAvailabilityListener(@NonNull AvailabilityListener<T> listener);

    void removeAvailabilityListener(@NonNull AvailabilityListener<T> listener);

    interface AvailabilityListener<T extends Availability> {
        void onAvailabilityChanged(@NonNull T object);
    }
}
