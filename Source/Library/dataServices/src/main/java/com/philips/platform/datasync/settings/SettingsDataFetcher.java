/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.characteristics.UserCharacteristicsConverter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class SettingsDataFetcher extends DataFetcher {
    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final SettingsConverter settingsConverter;

    @Inject
    UserCharacteristicsConverter mUserCharacteristicsConverter;

    @Inject
    Eventing eventing;

    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    @Inject
    public SettingsDataFetcher(@NonNull UCoreAdapter uCoreAdapter, @NonNull GsonConverter gsonConverter, @NonNull SettingsConverter settingsConverter) {
        super(uCoreAdapter);
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        this.settingsConverter = settingsConverter;
        DataServicesManager.getInstance().getAppComponant().injectSettingsDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchData() {

        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            getSettings();
        }
        return null;
    }

    public void getSettings() {

        if (isUserInvalid()) {
            postError(1, getNonLoggedInError());
            return;
        }

        SettingsClient client = uCoreAdapter.getAppFrameworkClient(SettingsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
        if (client == null) return;

        try {
            UCoreSettings settings = client.getSettings(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(), UCoreAdapter.API_VERSION);
            Settings appSettings = settingsConverter.convertUcoreToAppSettings(settings);
            if (appSettings != null) {
                eventing.post(new SettingsBackendSaveResponse(appSettings));
            }
        } catch (RetrofitError retrofitError) {
            eventing.post(new BackendDataRequestFailed(retrofitError));
        }

    }

    private void postError(int referenceId, final RetrofitError error) {
        eventing.post(new BackendResponse(referenceId, error));
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    public boolean isUserInvalid() {
        if (uCoreAccessProvider != null) {
            String accessToken = uCoreAccessProvider.getAccessToken();
            return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
        }
        return false;
    }
}
