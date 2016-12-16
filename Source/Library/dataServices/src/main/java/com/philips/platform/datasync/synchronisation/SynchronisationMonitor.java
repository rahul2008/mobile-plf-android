/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SynchronisationMonitor extends EventMonitor {

    @NonNull
    private final DataPullSynchronise pullSynchronise;

    @NonNull
    private final DataPushSynchronise pushSynchronise;

    @Singleton
    @Inject
    public SynchronisationMonitor(@NonNull final DataPullSynchronise pullSynchronise, final @NonNull DataPushSynchronise pushSynchronise) {
        this.pullSynchronise = pullSynchronise;
        this.pushSynchronise = pushSynchronise;
    }

    public void onEventAsync(ReadDataFromBackendRequest event) {
        synchronized (this) {
            if (DataServicesManager.getInstance().isPushComplete() && DataServicesManager.getInstance().isPullComplete()) {
                DataServicesManager.getInstance().setPullComplete(false);
                DSLog.i("***SPO***","In Synchronization Monitor onEventAsync - ReadDataFromBackenedRequest");
                pullSynchronise.startSynchronise(event.getLastSynchronizationTimestamp(), event.getEventId());
            }
        }
    }

    public void onEventAsync(WriteDataToBackendRequest event) {
        synchronized (this) {
            //TODO: also should pull new data from BE
            if (DataServicesManager.getInstance().isPullComplete() && DataServicesManager.getInstance().isPushComplete()) {
                DataServicesManager.getInstance().setPushComplete(false);
                DSLog.i("***SPO***", "In Synchronization Monitor onEventAsync - WriteDataToBackendRequest");
                pushSynchronise.startSynchronise(event.getEventId());
            }
        }
    }
}