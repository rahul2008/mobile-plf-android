/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.monitors.EventMonitor;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SynchronisationMonitor extends EventMonitor {

    @NonNull
    private final DataPullSynchronise pullSynchronise;

    @NonNull
    private final DataPushSynchronise pushSynchronise;

    @Inject
    public SynchronisationMonitor(@NonNull final DataPullSynchronise pullSynchronise, final @NonNull DataPushSynchronise pushSynchronise) {
        this.pullSynchronise = pullSynchronise;
        this.pushSynchronise = pushSynchronise;
    }

    public void onEventAsync(ReadDataFromBackendRequest event) {
        Log.i("***SPO***","In Synchronization Monitor onEventAsync - ReadDataFromBackenedRequest");
        pullSynchronise.startSynchronise(event.getLastSynchronizationTimestamp(), event.getEventId());
    }

    public void onEventAsync(WriteDataToBackendRequest event) {
        //TODO: also should pull new data from BE
        pushSynchronise.startSynchronise(event.getEventId());
    }
}