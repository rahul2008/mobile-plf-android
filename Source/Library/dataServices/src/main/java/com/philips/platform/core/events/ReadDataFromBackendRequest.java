/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.events;

import android.support.annotation.Nullable;

import org.joda.time.DateTime;

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
