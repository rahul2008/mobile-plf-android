/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.events.FetchByDateRange;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SynchronisationMonitor extends EventMonitor {

    @Inject
    DataPullSynchronise pullSynchronise;

    @Inject
    DataPushSynchronise pushSynchronise;

    @Inject
    public SynchronisationMonitor() {
        DataServicesManager.getInstance().getAppComponent().injectSynchronizationMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ReadDataFromBackendRequest event) {
        synchronized (this) {
            pullSynchronise.startSynchronise(event.getEventId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(FetchByDateRange fetchByDateRange) {
        synchronized (this) {
            pullSynchronise.startSynchronise(fetchByDateRange.getStartDate(), fetchByDateRange.getEndDate(), fetchByDateRange.getEventId());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(WriteDataToBackendRequest event) {
        synchronized (this) {
            pushSynchronise.startSynchronise(event.getEventId());
        }
    }
}