/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.synchronisation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.ErrorHandlingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.userprofile.UserRegistrationInterface;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import retrofit.RetrofitError;

public abstract class DataFetcher {
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
    public abstract RetrofitError fetchData();

    public RetrofitError fetchAllData() {
        return fetchData();
    }

    public void onError(RetrofitError error) {
        synchronisationManager.dataPullFail(error);
        refreshTokenIfTokenExpired(error);
    }

    private boolean refreshTokenIfTokenExpired(RetrofitError e) {
        int status = -1000;
        if (e != null && e.getResponse() != null) {
            status = e.getResponse().getStatus();
        }

        if (status == HttpURLConnection.HTTP_UNAUTHORIZED ||
                status == HttpURLConnection.HTTP_FORBIDDEN) {
            userRegistrationInterface.refreshAccessTokenUsingWorkAround();
            return true;
        }
        return false;
    }
}

