/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.datasync.UCoreAccessProvider;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Singleton;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
public class DataPushSynchronise extends EventMonitor {

    @NonNull
    private final UCoreAccessProvider accessProvider;

    @NonNull
    private final List<? extends com.philips.platform.datasync.synchronisation.DataSender> senders;

    @NonNull
    private final Executor executor;

    @NonNull
    private final Eventing eventing;

    @NonNull
    private final AtomicInteger numberOfRunningFetches = new AtomicInteger(0);

    public DataPushSynchronise(@NonNull final UCoreAccessProvider accessProvider,
                               @NonNull final List<? extends com.philips.platform.datasync.synchronisation.DataSender> senders,
                               @NonNull final Executor executor,
                               @NonNull final Eventing eventing) {
        this.accessProvider = accessProvider;
        this.senders = senders;
        this.executor = executor;
        this.eventing = eventing;
    }

    public void startSynchronise(final int eventId) {
        Log.i("***SPO***","In startSynchronise - DataPushSynchronize");
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if(!isLoggedIn){
            Log.i("***SPO***","DataPushSynchronize isLogged-in is false");
            eventing.post(new BackendResponse(eventId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in"))));
            return;
        }

        if (!isSyncStarted()) {
            Log.i("***SPO***","DataPushSynchronize isLogged-in is true");
            registerEvent();
            fetchNonSynchronizedData(eventId);
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


    private void fetchNonSynchronizedData(int eventId) {
        Log.i("***SPO***","DataPushSynchronize fetchNonSynchronizedData before calling GetNonSynchronizedDataRequest");
        eventing.post(new GetNonSynchronizedDataRequest(eventId));
    }

    public void onEventAsync(GetNonSynchronizedDataResponse response) {
        Log.i("***SPO***","DataPushSynchronize GetNonSynchronizedDataResponse");
        numberOfRunningFetches.set(1);
        startAllSenders(response);
    }

    private boolean isSyncStarted() {
        boolean isSynced = numberOfRunningFetches.get() > 0;
        Log.i("***SPO***",isSynced + "");
        return isSynced;
    }

    private void startAllSenders(final GetNonSynchronizedDataResponse nonSynchronizedData) {
        Log.i("***SPO***","DataPushSynchronize startAllSenders");
        for (final com.philips.platform.datasync.synchronisation.DataSender sender : senders) {
            Log.i("***SPO***","DataPushSynchronize startAllSenders inside loop");
            //startSenders(sender, nonSynchronizedData);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i("***SPO***","DataPushSynchronize before sendDataToBackend");
                    startSenders(sender, nonSynchronizedData);
                }
            }).start();

      /*      executor.execute(new Runnable() {
                @Override
                public void run() {
                    startSenders(sender, nonSynchronizedData);
                }
            });
      */      //Log.i("***SPO***","DataPushSynchronize before sendDataToBackend");
            //sender.sendDataToBackend(nonSynchronizedData.getDataToSync(sender.getClassForSyncData()));
        }
    }

    private void startSenders(DataSender sender, GetNonSynchronizedDataResponse nonSynchronizedData) {
        Log.i("***SPO***","DataPushSynchronize before sendDataToBackend");
        int jobsRunning = numberOfRunningFetches.decrementAndGet();
        Log.i("**SPO**","In DataPushSynchronize executor and jobsRunning = " + jobsRunning);

        if (jobsRunning == 0) {
            Log.i("**SPO**","In DataPushSynchronize and jobsRunning = " + jobsRunning + "calling sendDataToBackend");
            sender.sendDataToBackend(nonSynchronizedData.getDataToSync(sender.getClassForSyncData()));
        }
    }
}