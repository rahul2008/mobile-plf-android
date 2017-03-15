/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
public class SynchronisationMonitor extends EventMonitor {

    @Inject
    DataPullSynchronise pullSynchronise;

    @Inject
    DataPushSynchronise pushSynchronise;


    @Inject
    public SynchronisationMonitor() {
        DataServicesManager.getInstance().getAppComponant().injectSynchronizationMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ReadDataFromBackendRequest event) {
        synchronized (this) {
  //          if (DataServicesManager.getInstance().isPushComplete() && DataServicesManager.getInstance().isPullComplete()) {
    //            DataServicesManager.getInstance().setPullComplete(false);
                DSLog.i(DSLog.LOG,"In Synchronization Monitor onEventAsync - ReadDataFromBackenedRequest");
                pullSynchronise.startSynchronise(event.getLastSynchronizationTimestamp(), event.getEventId());
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(WriteDataToBackendRequest event) {
        synchronized (this) {
            //if (DataServicesManager.getInstance().isPullComplete() && DataServicesManager.getInstance().isPushComplete()) {
              //  DataServicesManager.getInstance().setPushComplete(false);
                DSLog.i(DSLog.LOG, "In Synchronization Monitor onEventAsync - WriteDataToBackendRequest");
                pushSynchronise.startSynchronise(event.getEventId());
         //   }
        }
    }
}