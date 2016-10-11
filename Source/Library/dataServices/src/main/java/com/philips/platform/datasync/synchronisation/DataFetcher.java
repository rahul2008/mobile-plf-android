/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.joda.time.DateTime;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class DataFetcher {
    @NonNull
    protected final UCoreAdapter uCoreAdapter;

    @NonNull
    protected final UCoreAccessProvider accessProvider;

    @NonNull
    protected final Eventing eventing;

    public DataFetcher(@NonNull final UCoreAdapter uCoreAdapter,
                       @NonNull final UCoreAccessProvider accessProvider,
                       @NonNull final Eventing eventing) {
        this.uCoreAdapter = uCoreAdapter;
        this.accessProvider = accessProvider;
        this.eventing = eventing;
    }

    @CheckResult
    @Nullable
    public abstract RetrofitError fetchDataSince(@Nullable final DateTime sinceTimestamp);

    public RetrofitError fetchAllData() {
        return fetchDataSince(null);
    }

    protected boolean isUserInvalid() {
        final String accessToken = accessProvider.getAccessToken();
        return !accessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }
}

