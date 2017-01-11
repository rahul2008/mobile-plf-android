/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.events.WriteDataToBackendRequest;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.consent.ConsentsDataFetcher;
import com.philips.platform.datasync.moments.MomentsDataFetcher;

import org.joda.time.DateTime;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@SuppressWarnings("unchecked")
public class DataPullSynchronise {

    @Inject
    UCoreAccessProvider accessProvider;

    @Nullable
    private DateTime lastSyncDateTime;
    private int referenceId;

    @NonNull
    MomentsDataFetcher mMomentsDataFetcher;

    DBRequestListener mDbRequestListener;

    @NonNull
    private final Executor executor;

    @NonNull
    private final List<? extends com.philips.platform.datasync.synchronisation.DataFetcher> fetchers;

    @Inject
    Eventing eventing;

    private volatile RetrofitError fetchResult;

    @NonNull
    private final AtomicInteger numberOfRunningFetches = new AtomicInteger(0);

    DataServicesManager mDataServicesManager;

    @Inject
    public DataPullSynchronise(@NonNull final List<? extends com.philips.platform.datasync.synchronisation.DataFetcher> fetchers,
                               @NonNull final Executor executor) {
        mDataServicesManager = DataServicesManager.getInstance();
        mDbRequestListener = mDataServicesManager.getDbRequestListener();
        mDataServicesManager.mAppComponent.injectDataPullSynchronize(this);
        this.fetchers = fetchers;
        this.executor = executor;
    }


    private boolean isSyncStarted() {
        return numberOfRunningFetches.get() > 0;
    }


    public void startFetching(final DateTime lastSyncDateTime, final int referenceId, final DataFetcher fetcher) {
        DSLog.i("**SPO**","In Data Pull Synchronize startFetching");
        preformFetch(fetcher, lastSyncDateTime, referenceId);
      /*  executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.i("**SPO**","In Data Pull Synchronize startFetching execute");
                preformFetch(fetcher, lastSyncDateTime, referenceId);
            }
        });*/
    }

    public void startSynchronise(@Nullable final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i("***SPO***","In startSynchronise - DataPullSynchronize");
        this.lastSyncDateTime = lastSyncDateTime;
        this.referenceId = referenceId;
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if(!isLoggedIn){
            DSLog.i("***SPO***","DataPullSynchronize isLogged-in is false");
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
            return;
        }

        if (!isSyncStarted()) {
            DSLog.i("***SPO***","DataPullSynchronize isLogged-in is true");
            registerEvent();
            DSLog.i("***SPO***","Before calling GetNonSynchronizedMomentsRequest");
            eventing.post(new GetNonSynchronizedMomentsRequest(mDbRequestListener));
        }
    }

    public void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

    private void preformFetch(final DataFetcher fetcher, final DateTime lastSyncDateTime, final int referenceId) {
        DSLog.i("**SPO**","In Data Pull Synchronize preformFetch");
        RetrofitError resultError = fetcher.fetchDataSince(lastSyncDateTime);
        updateResult(resultError);

        int jobsRunning = numberOfRunningFetches.decrementAndGet();
        DSLog.i("**SPO**","In Data Pull Synchronize preformFetch and jobsRunning = " + jobsRunning);

        if (jobsRunning == 0) {
            DSLog.i("**SPO**","In Data Pull Synchronize preformFetch and jobsRunning = " + jobsRunning + "calling report result");
            reportResult(fetchResult, referenceId);
        }
    }

    private void updateResult(final RetrofitError resultError) {
        if (resultError != null) {
            this.fetchResult = resultError;
        }
    }

    private void reportResult(final RetrofitError result, final int referenceId) {
        DSLog.i("**SPO**","In Data Pull Synchronize reportResult");
        if (result == null) {
            DSLog.i("**SPO**","In Data Pull Synchronize reportResult is OK call postOK");
            postOk(referenceId);
        } else {
            DSLog.i("**SPO**","In Data Pull Synchronize reportResult is not OK call postError");
            postError(referenceId, result);
        }
    }

    private void postError(final int referenceId, final RetrofitError error) {
        DSLog.i("**SPO**","Error DataPullSynchronize postError" + error.getMessage());
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void postOk(final int referenceId) {
        DSLog.i("**SPO**","In Data Pull Synchronize postOK");
        eventing.post(new ReadDataFromBackendResponse(referenceId,mDataServicesManager.getDbRequestListener()));
    }

    private void initFetch() {
        DSLog.i("**SPO**","In Data Pull Synchronize initFetch");
        numberOfRunningFetches.set(1);
        fetchResult = null;
    }

    private void fetchData(final DateTime lastSyncDateTime, final int referenceId, final List<? extends Moment> nonSynchronizedMoments ,final List<? extends ConsentDetail> consentDetails) {
        DSLog.i("**SPO**","In Data Pull Synchronize fetchData");
        initFetch();
        for(DataFetcher fetcher:fetchers){
            /*if ((fetcher instanceof MomentsDataFetcher || fetcher instanceof ConsentsDataFetcher && nonSynchronizedMoments != null && !nonSynchronizedMoments.isEmpty())) {
                numberOfRunningFetches.decrementAndGet();
                continue;
            }*/

            if(fetcher instanceof ConsentsDataFetcher){
                ((ConsentsDataFetcher) fetcher).setConsentDetails((List<ConsentDetail>) consentDetails);
            }
            //if(fetcher instanceof  MomentsDataFetcher) {
                startFetching(lastSyncDateTime, referenceId, fetcher);
            //}
        //startFetching(lastSyncDateTime, referenceId, mMomentsDataFetcher);
    }}

    public void onEventAsync(GetNonSynchronizedMomentsResponse response) {
        synchronized (this) {
            DSLog.i("**SPO**", "In Data Pull Synchronize GetNonSynchronizedMomentsResponse");
            final List<? extends Moment> nonSynchronizedMoments = response.getNonSynchronizedMoments();
            fetchData(lastSyncDateTime, referenceId, nonSynchronizedMoments, response.getConsentDetails());
        }
        mDataServicesManager.setPullComplete(true);
        eventing.post(new WriteDataToBackendRequest());
       // unRegisterEvent();
    }
}
