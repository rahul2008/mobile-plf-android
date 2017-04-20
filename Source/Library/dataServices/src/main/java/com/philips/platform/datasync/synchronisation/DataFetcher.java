/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import org.joda.time.DateTime;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class DataFetcher {
    int UNKNOWN = -1;

    @NonNull
    protected final UCoreAdapter uCoreAdapter;

    @Inject
    public SynchronisationManager synchronisationManager;

    @Inject
    public UserRegistrationInterface userRegistrationInterface;

    @Inject
    ErrorHandlingInterface errorHandlingInterface;

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
        synchronisationManager.dataPullFail(error);
        refreshTokenIfTokenExpired(error);
    }

    private boolean refreshTokenIfTokenExpired(RetrofitError e) {
        DSLog.i("UserRegistrationInterfaceImpl", "Check is token valid in MomentDataFetcher");
        int status = -1000;
        if (e != null && e.getResponse() != null) {
            status = e.getResponse().getStatus();
        }

        if (status == HttpURLConnection.HTTP_UNAUTHORIZED ||
                status == HttpURLConnection.HTTP_FORBIDDEN) {
            DSLog.i("UserRegistrationInterfaceImpl", "Call refresh token using work around");
            userRegistrationInterface.refreshAccessTokenUsingWorkAround();
            return true;
        }
        return false;
    }
}

