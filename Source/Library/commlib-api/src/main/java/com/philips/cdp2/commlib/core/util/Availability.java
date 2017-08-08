/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.util;

import android.support.annotation.NonNull;

public interface Availability {

    boolean isAvailable();

    void addAvailabilityListener(@NonNull AvailabilityListener listener);

    void removeAvailabilityListener(@NonNull AvailabilityListener listener);

    interface AvailabilityListener {
        void onAvailabilityChanged(@NonNull Availability object);
    }
}
