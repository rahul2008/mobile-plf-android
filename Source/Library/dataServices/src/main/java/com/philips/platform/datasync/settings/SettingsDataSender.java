package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.monitors.EventMonitor;
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

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
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
        DataServicesManager.getInstance().getAppComponant().injectSettingsDataSender(this);
    }

    @Override
    public boolean sendDataToBackend(@NonNull List dataToSend) {

        if (dataToSend!=null && !dataToSend.isEmpty() && synchronizationState.get() != State.BUSY.getCode()) {
            for(Object settings:dataToSend){
                sendSettingsToBackend((Settings)settings);
            }

        }

        return false;
    }

    @Override
    public Class<? extends Settings> getClassForSyncData() {
        return Settings.class;
    }

    public void sendSettingsToBackend(Settings settings) {
        if (isUserInvalid()) {
            postError(1, getNonLoggedInError());
            return;
        }
        if (uCoreAccessProvider == null) {
            return;
        }

        try {
            UCoreSettings uCoreSettings = settingsConverter.convertAppToUcoreSettings(settings);
            SettingsClient appFrameworkClient = uCoreAdapter.getAppFrameworkClient(SettingsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
            Response response = appFrameworkClient.updateSettings(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(), uCoreSettings);

            if (isResponseSuccess(response)) {
                eventing.post(new SyncBitUpdateRequest(OrmTableType.SETTINGS, true));
            }

        } catch (RetrofitError retrofitError) {
            eventing.post(new BackendResponse(1, retrofitError));
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