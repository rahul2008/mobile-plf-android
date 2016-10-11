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
    private final Eventing eventing;

    private volatile RetrofitError fetchResult;

    @NonNull
    private final AtomicInteger numberOfRunningFetches = new AtomicInteger(0);

    @Inject
    public DataPullSynchronise(@NonNull final UCoreAccessProvider accessProvider,
                               @NonNull final MomentsDataFetcher fetcher,
                               @NonNull final Executor executor,
                               @NonNull final Eventing  eventing) {
        this.accessProvider = accessProvider;
        this.mMomentsDataFetcher = fetcher;
        this.executor = executor;
        this.eventing = eventing;
    }


    private boolean isSyncStarted() {
        return numberOfRunningFetches.get() > 0;
    }





    public void startFetching(final DateTime lastSyncDateTime, final int referenceId, final DataFetcher fetcher) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                preformFetch(fetcher, lastSyncDateTime, referenceId);
            }
        });
    }

    public void startSynchronise(@Nullable final DateTime lastSyncDateTime, final int referenceId) {
        this.lastSyncDateTime = lastSyncDateTime;
        this.referenceId = referenceId;
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if (isLoggedIn) {
            registerEvent();
            eventing.post(new GetNonSynchronizedMomentsRequest());
        } else  {
            postError(referenceId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in")));
        }
    }

    public void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

    public void unRegisterEvent() {
        if (eventing.isRegistered(this)) {
            eventing.unregister(this);
        }
    }

    private void preformFetch(final DataFetcher fetcher, final DateTime lastSyncDateTime, final int referenceId) {
        RetrofitError resultError = fetcher.fetchDataSince(lastSyncDateTime);
        updateResult(resultError);

        int jobsRunning = numberOfRunningFetches.decrementAndGet();

        if (jobsRunning == 0) {
            reportResult(fetchResult, referenceId);
        }
    }

    private void updateResult(final RetrofitError resultError) {
        if (resultError != null) {
            this.fetchResult = resultError;
        }
    }

    private void reportResult(final RetrofitError result, final int referenceId) {
        if (result == null) {
            postOk(referenceId);
        } else {
            postError(referenceId, result);
        }
    }

    private void postError(final int referenceId, final RetrofitError error) {
        Log.i("***","Error" + error.getMessage());
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void postOk(final int referenceId) {
        eventing.post(new ReadDataFromBackendResponse(referenceId));
    }

    private void initFetch() {
        numberOfRunningFetches.set(1);
        fetchResult = null;
    }

    private void fetchData(final DateTime lastSyncDateTime, final int referenceId, final List<? extends Moment> nonSynchronizedMoments) {
        initFetch();
        startFetching(lastSyncDateTime, referenceId, mMomentsDataFetcher);
    }

    public void onEventAsync(GetNonSynchronizedMomentsResponse response) {
        final List<? extends Moment> nonSynchronizedMoments = response.getNonSynchronizedMoments();
        fetchData(lastSyncDateTime, referenceId, nonSynchronizedMoments);
        unRegisterEvent();
    }
}
