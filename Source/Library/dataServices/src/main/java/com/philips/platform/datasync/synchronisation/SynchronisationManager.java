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

import org.joda.time.DateTime;

import javax.inject.Inject;

public class SynchronisationManager implements SynchronisationChangeListener {

    private volatile boolean isSyncComplete = true;

    SynchronisationCompleteListener mSynchronisationCompleteListener;

    @Inject
    Eventing mEventing;

    public SynchronisationManager() {
        DataServicesManager.getInstance().getAppComponant().injectSynchronisationManager(this);
    }

    public void startSync(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListner) {
        synchronized (this) {
            mSynchronisationCompleteListener = synchronisationCompleteListner;
            if (isSyncComplete) {
                isSyncComplete = false;
                if (startDate == null || endDate == null) {
                    mEventing.post(new ReadDataFromBackendRequest());
                } else {
                    mEventing.post(new FetchByDateRange(startDate.toString(), endDate.toString()));
                }
            }
        }
    }

    @Override
    public void dataPullSuccess() {
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
