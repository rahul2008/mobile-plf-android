package com.philips.platform.core.events;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ReadDataFromBackendRequest extends Event {

    @Nullable
    private DateTime lastSynchronizationTimestamp;

    public ReadDataFromBackendRequest(@Nullable final DateTime lastSynchronizationTimestamp) {
        super();
        this.lastSynchronizationTimestamp = lastSynchronizationTimestamp;
    }

    @Nullable
    public DateTime getLastSynchronizationTimestamp() {
        return lastSynchronizationTimestamp;
    }
}
