/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.GetNonSynchronizedDataRequest;
import com.philips.platform.core.events.GetNonSynchronizedDataResponse;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.datasync.UCoreAccessProvider;

import java.util.List;
import java.util.concurrent.Executor;

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
        boolean isLoggedIn = accessProvider.isLoggedIn();

        if (isLoggedIn) {
            fetchNonSynchronizedData(eventId);
        } else {
            eventing.post(new BackendResponse(eventId, RetrofitError.unexpectedError("", new IllegalStateException("You're not logged in"))));
        }
    }

    private void fetchNonSynchronizedData(int eventId) {
        eventing.post(new GetNonSynchronizedDataRequest(eventId));
    }

    public void onEventAsync(GetNonSynchronizedDataResponse response) {
        startAllSenders(response);
    }

    private void startAllSenders(final GetNonSynchronizedDataResponse nonSynchronizedData) {
        for (final com.philips.platform.datasync.synchronisation.DataSender sender : senders) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    sender.sendDataToBackend(nonSynchronizedData.getDataToSync(sender.getClassForSyncData()));
                }
            });
        }
    }
}