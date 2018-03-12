/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.synchronisation;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.DeleteExpiredInsightRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.FetchByDateRange;
import com.philips.platform.core.events.ReadDataFromBackendRequest;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.listeners.SynchronisationChangeListener;
import com.philips.platform.core.listeners.SynchronisationCompleteListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.exception.SyncException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

import javax.inject.Inject;

public class SynchronisationManager implements SynchronisationChangeListener {

    private static final String LAST_EXPIRED_DELETION_DATE_TIME = "LAST_EXPIRED_DELETION_DATE_TIME";
    private volatile boolean isSyncComplete = true;

    SynchronisationCompleteListener mSynchronisationCompleteListener;

    @Inject
    Eventing mEventing;

    SharedPreferences expiredDeletionTimeStorage;

    public SynchronisationManager() {
        DataServicesManager.getInstance().getAppComponent().injectSynchronisationManager(this);
        expiredDeletionTimeStorage = DataServicesManager.getInstance().getDataServiceContext().getSharedPreferences(LAST_EXPIRED_DELETION_DATE_TIME, Context.MODE_PRIVATE);
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

    public void stopSync() {
        isSyncComplete = true;
        mSynchronisationCompleteListener = null;
    }

    @Override
    public void dataPullSuccess() {
        DateTime lastDeletionTime = getLastExpiredDataDeletionDateTime();
        DateTime now = DateTime.now();

        if(now.isAfter(lastDeletionTime.plusDays(1))) {
            // 24 hour or more have passed
            clearExpiredMoments(new DBRequestListener<Integer>() {
                @Override
                public void onSuccess(List<? extends Integer> data) {
                    clearExpiredInsights(new DBRequestListener<Insight>() {
                        @Override
                        public void onSuccess(List<? extends Insight> data) {
                            setLastExpiredDataDeletionDateTime();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            // NOP
                        }
                    });
                }

                @Override
                public void onFailure(Exception exception) {
                    // NOP
                }
            });
        }

        // Always
        postEventToStartPush();
    }

    @Override
    public void dataPushSuccess() { }

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

    private boolean isSyncInProcess() {
        synchronized (this) {
            if (isSyncComplete) {
                isSyncComplete = false;
                return false;
            }
            return true;
        }
    }

    private void postEvent(DateTime startDate, DateTime endDate, SynchronisationCompleteListener synchronisationCompleteListener) {
        if (!isSyncInProcess()) {
            this.mSynchronisationCompleteListener = synchronisationCompleteListener;
            mEventing.post(new FetchByDateRange(startDate.toString(), endDate.toString()));
        } else {
            synchronisationCompleteListener.onSyncFailed(new SyncException("Sync is already in progress"));
        }
    }

    private void postEventToStartPush() {
        mEventing.post(new WriteDataToBackendRequest());
    }

    private void clearExpiredMoments(DBRequestListener<Integer> listener) {
        mEventing.post(new DeleteExpiredMomentRequest(listener));
    }


    private void clearExpiredInsights(DBRequestListener<Insight> listener) {
        mEventing.post(new DeleteExpiredInsightRequest(listener));
    }

    private void setLastExpiredDataDeletionDateTime() {
        expiredDeletionTimeStorage.edit().putString(LAST_EXPIRED_DELETION_DATE_TIME, DateTime.now(DateTimeZone.UTC).toString()).apply();
    }

    private DateTime getLastExpiredDataDeletionDateTime() {
        String lastDeletion = expiredDeletionTimeStorage.getString(LAST_EXPIRED_DELETION_DATE_TIME, "02-01-1970");
        return DateTime.parse(lastDeletion);
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
}
