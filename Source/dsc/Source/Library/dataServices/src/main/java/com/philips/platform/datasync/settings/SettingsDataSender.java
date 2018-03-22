/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SettingsDataSender extends DataSender {

    @Inject
    Eventing eventing;

    @NonNull
    final AtomicInteger synchronizationState = new AtomicInteger(0);

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @Inject
    UCoreAccessProvider uCoreAccessProvider;


    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final SettingsConverter settingsConverter;

    @Inject
    public SettingsDataSender(@NonNull UCoreAdapter uCoreAdapter, @NonNull GsonConverter gsonConverter, @NonNull SettingsConverter settingsConverter) {
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        this.settingsConverter = settingsConverter;
        DataServicesManager.getInstance().getAppComponent().injectSettingsDataSender(this);
    }

    @Override
    public boolean sendDataToBackend(@NonNull List dataToSend) {
        if (dataToSend != null && !dataToSend.isEmpty() && synchronizationState.get() != State.BUSY.getCode()) {
            for (Object settings : dataToSend) {
                sendSettingsToBackend((Settings) settings);
            }
        }
        return false;
    }

    @Override
    public Class<? extends Settings> getClassForSyncData() {
        return Settings.class;
    }

    void sendSettingsToBackend(Settings settings) {

        if (settings == null) return;

        if (isUserInvalid()) {
            postError(1, getNonLoggedInError());
            return;
        }

        try {
            UCoreSettings uCoreSettings = settingsConverter.convertAppToUcoreSettings(settings);
            SettingsClient appFrameworkClient = uCoreAdapter.getAppFrameworkClient(SettingsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
            if (appFrameworkClient == null) return;
            Response response = appFrameworkClient.updateSettings(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(), uCoreSettings);

            if (isResponseSuccess(response)) {
                eventing.post(new SyncBitUpdateRequest(SyncType.SETTINGS, true));
            }

        } catch (RetrofitError retrofitError) {
            eventing.post(new BackendResponse(1, retrofitError));
            onError(retrofitError);
        }

    }

    public boolean isUserInvalid() {
        if (uCoreAccessProvider != null) {
            String accessToken = uCoreAccessProvider.getAccessToken();
            return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
        }
        return false;
    }

    private boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    private void postError(int referenceId, final RetrofitError error) {
        eventing.post(new BackendResponse(referenceId, error));
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

}