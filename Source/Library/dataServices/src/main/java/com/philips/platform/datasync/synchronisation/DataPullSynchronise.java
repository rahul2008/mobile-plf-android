/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
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
public class DataPullSynchronise {

    @NonNull
    private final UCoreAccessProvider accessProvider;

    @Nullable
    private DateTime lastSyncDateTime;
    private int referenceId;

    @NonNull
    MomentsDataFetcher mMomentsDataFetcher;

    @NonNull
    private final Executor executor;

    @NonNull
    private final List<? extends com.philips.platform.datasync.synchronisation.DataFetcher> fetchers;

    @NonNull
    private final Eventing eventing;

    private volatile RetrofitError fetchResult;

    @NonNull
    private final AtomicInteger numberOfRunningFetches = new AtomicInteger(0);

    DataServicesManager mDataServicesManager;

    @Inject
    public DataPullSynchronise(@NonNull final List<? extends com.philips.platform.datasync.synchronisation.DataFetcher> fetchers,
                               @NonNull final Executor executor,
                               @NonNull final Eventing  eventing) {
        mDataServicesManager = DataServicesManager.getInstance();
        this.accessProvider = mDataServicesManager.getUCoreAccessProvider();
        this.fetchers = fetchers;
        this.executor = executor;
        this.eventing = eventing;
    }


    private boolean isSyncStarted() {
        return numberOfRunningFetches.get() > 0;
    }


    public void startFetching(final DateTime lastSyncDateTime, final int referenceId, final DataFetcher fetcher) {
        Log.i("**SPO**","In Data Pull Synchronize startFetching");
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
        Log.i("***SPO***","In startSynchronise - DataPullSynchronize");
        this.lastSyncDateTime = lastSyncDateTime;
        this.referenceId = referenceId;
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if(!isLoggedIn){
            Log.i("***SPO***","DataPullSynchronize isLogged-in is false");
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
            return;
        }

        if (!isSyncStarted()) {
            Log.i("***SPO***","DataPullSynchronize isLogged-in is true");
            registerEvent();
            Log.i("***SPO***","Before calling GetNonSynchronizedMomentsRequest");
            eventing.postSticky(new GetNonSynchronizedMomentsRequest());
        }
    }

    public void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.registerSticky(this);
        }
    }

 /*   public void unRegisterEvent() {
        if (eventing.isRegistered(this)) {
            eventing.unregister(this);
        }
    }*/

    private void preformFetch(final DataFetcher fetcher, final DateTime lastSyncDateTime, final int referenceId) {
        Log.i("**SPO**","In Data Pull Synchronize preformFetch");
        RetrofitError resultError = fetcher.fetchDataSince(lastSyncDateTime);
        updateResult(resultError);

        int jobsRunning = numberOfRunningFetches.decrementAndGet();
        Log.i("**SPO**","In Data Pull Synchronize preformFetch and jobsRunning = " + jobsRunning);

        if (jobsRunning == 0) {
            Log.i("**SPO**","In Data Pull Synchronize preformFetch and jobsRunning = " + jobsRunning + "calling report result");
            reportResult(fetchResult, referenceId);
        }
    }

    private void updateResult(final RetrofitError resultError) {
        if (resultError != null) {
            this.fetchResult = resultError;
        }
    }

    private void reportResult(final RetrofitError result, final int referenceId) {
        Log.i("**SPO**","In Data Pull Synchronize reportResult");
        if (result == null) {
            Log.i("**SPO**","In Data Pull Synchronize reportResult is OK call postOK");
            postOk(referenceId);
        } else {
            Log.i("**SPO**","In Data Pull Synchronize reportResult is not OK call postError");
            postError(referenceId, result);
        }
    }

    private void postError(final int referenceId, final RetrofitError error) {
        Log.i("**SPO**","Error" + error.getMessage());
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void postOk(final int referenceId) {
        Log.i("**SPO**","In Data Pull Synchronize postOK");
        eventing.post(new ReadDataFromBackendResponse(referenceId));
    }

    private void initFetch() {
        Log.i("**SPO**","In Data Pull Synchronize initFetch");
        numberOfRunningFetches.set(1);
        fetchResult = null;
    }

    private void fetchData(final DateTime lastSyncDateTime, final int referenceId, final List<? extends Moment> nonSynchronizedMoments) {
        Log.i("**SPO**","In Data Pull Synchronize fetchData");
        initFetch();
        for(DataFetcher fetcher:fetchers){
            if(fetcher instanceof MomentsDataFetcher){
                startFetching(lastSyncDateTime, referenceId, fetcher);
            }else{
                startFetching(lastSyncDateTime, referenceId, fetcher);
            }
        }

    }

    public void onEventAsync(GetNonSynchronizedMomentsResponse response) {
        Log.i("**SPO**","In Data Pull Synchronize GetNonSynchronizedMomentsResponse");
        final List<? extends Moment> nonSynchronizedMoments = response.getNonSynchronizedMoments();
        fetchData(lastSyncDateTime, referenceId, nonSynchronizedMoments);
       // unRegisterEvent();
    }
}
