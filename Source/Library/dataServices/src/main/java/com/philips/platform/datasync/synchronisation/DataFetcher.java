/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.listeners.SynchronisationChangeListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAdapter;

import org.joda.time.DateTime;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class DataFetcher {
    @NonNull
    protected final UCoreAdapter uCoreAdapter;

    @Inject
    SynchronisationChangeListener synchronisationChangeListener;

    public DataFetcher(@NonNull final UCoreAdapter uCoreAdapter) {
        DataServicesManager.getInstance().getAppComponant().injectDataFetcher(this);
        this.uCoreAdapter = uCoreAdapter;
    }

    @CheckResult
    @Nullable
    public abstract RetrofitError fetchDataSince(@Nullable final DateTime sinceTimestamp);

    public RetrofitError fetchAllData() {
        return fetchDataSince(null);
    }

    public void onError(RetrofitError error){
        synchronisationChangeListener.dataPullFail(error);
    }
}

