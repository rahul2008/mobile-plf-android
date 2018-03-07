/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.synchronisation;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.FetchByDateRange;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.listeners.SynchronisationChangeListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.exception.SyncException;

import org.joda.time.DateTime;

import javax.inject.Inject;

public class SynchronisationManager implements SynchronisationChangeListener {

    private volatile boolean isSyncComplete = true;

    SynchronisationCompleteListener mSynchronisationCompleteListener;

    @Inject
    Eventing mEventing;

    public SynchronisationManager() {
        DataServicesManager.getInstance().getAppComponent().injectSynchronisationManager(this);
    }

    public void startSync(SynchronisationCompleteListener synchronisationCompleteListener) {
        if (!isSyncInProcess()) {
            this.mSynchronisationCompleteListener = synchronisationCompleteListener;
            mEventing.post(new ReadDataFromBackendRequest());
        }
    }

    public void startSync(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListener) {
        if (startDate == null || endDate == null) {
            synchronisationCompleteListener.onSyncFailed(new SyncException("Invalid Date Range"));
            return;
        }
        postEvent(startDate, endDate, synchronisationCompleteListener);
    }

    private void postEvent(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListener) {
        if (!isSyncInProcess()) {
            this.mSynchronisationCompleteListener = synchronisationCompleteListener;
            mEventing.post(new FetchByDateRange(startDate.toString(), endDate.toString()));
        } else {
            synchronisationCompleteListener.onSyncFailed(new SyncException("Sync is already in progress"));
        }
    }

    private boolean isSyncInProcess() {
        synchronized (this) {
            if (isSyncComplete) {
                isSyncComplete = false;
                return false;
            }
            return true;
        }
    }


    @Override
    public void dataPullSuccess() {
        // TODO Before starting to push, delete all 'expired' data (Moments & Insights)
        postEventToStartPush();
    }

    @Override
    public void dataPushSuccess() {

    }

    @Override
    public void dataPullFail(Exception e) {
        postOnSyncFailed(e);
    }

    @Override
    public void dataPushFail(Exception e) {
        postOnSyncFailed(e);
    }

    @Override
    public void dataSyncComplete() {
        postOnSyncComplete();
    }

    private void postEventToStartPush() {
        mEventing.post(new WriteDataToBackendRequest());
    }

    private void postOnSyncComplete() {
        //System.out.println("Sync Complete");
        isSyncComplete = true;
        if (mSynchronisationCompleteListener != null)
            mSynchronisationCompleteListener.onSyncComplete();
        mSynchronisationCompleteListener = null;
    }

    private void postOnSyncFailed(Exception exception) {
        isSyncComplete = true;
        if (mSynchronisationCompleteListener != null)
            mSynchronisationCompleteListener.onSyncFailed(exception);
        mSynchronisationCompleteListener = null;
    }

    public void stopSync() {
        isSyncComplete = true;
        mSynchronisationCompleteListener = null;
    }
}
