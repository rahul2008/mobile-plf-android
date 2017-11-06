/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.events.FetchByDateRange;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.Synchronize;
import com.philips.platform.core.events.SynchronizeWithFetchByDateRange;
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
    SynchronisationManager synchronisationManager;

    @Inject
    public SynchronisationMonitor() {
        DataServicesManager.getInstance().getAppComponant().injectSynchronizationMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAsync(ReadDataFromBackendRequest event) {
        pullSynchronise.startSynchronise(event.getEventId());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(WriteDataToBackendRequest event) {
        pushSynchronise.startSynchronise(event.getEventId());
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventAsync(FetchByDateRange fetchByDateRange) {
        pullSynchronise.startSynchronise(fetchByDateRange.getStartDate(), fetchByDateRange.getEndDate(), fetchByDateRange.getEventId());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(Synchronize synchronize) {
        synchronisationManager.startSync(synchronize.getStartDate(), synchronize.getEndDate(), synchronize.getSynchronisationCompleteListener());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(SynchronizeWithFetchByDateRange synchronizeWithFetchByDateRange) {
        synchronisationManager.startSync(synchronizeWithFetchByDateRange.getStartDate(), synchronizeWithFetchByDateRange.getEndDate(),
                synchronizeWithFetchByDateRange.getSynchronisationCompleteListener());
    }
}