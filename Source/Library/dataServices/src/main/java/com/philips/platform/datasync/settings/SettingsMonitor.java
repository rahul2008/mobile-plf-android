package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.SettingsBackendGetRequest;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SettingsMonitor extends EventMonitor {

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @Inject
    UCoreAccessProvider uCoreAccessProvider;


    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final SettingsConverter settingsConverter;



    @Inject
    public SettingsMonitor(@NonNull final UCoreAdapter uCoreAdapter,
                           @NonNull final SettingsConverter settingsConverter,
                           @NonNull final GsonConverter gsonConverter) {
        DataServicesManager.getInstance().getAppComponant().injectUserSettingsMonitor(this);
        this.uCoreAdapter = uCoreAdapter;
        this.settingsConverter = settingsConverter;
        this.gsonConverter = gsonConverter;
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(SettingsBackendSaveRequest event) {
        sendToBackend(event);
        System.out.println("In onEventAsync(SettingsBackendSaveRequest event) ");
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(SettingsBackendGetRequest event) {
        getSettings(event);
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    private void getSettings(SettingsBackendGetRequest event) {

        if (isUserInvalid()) {
            postError(event.getEventId(), getNonLoggedInError());
            return;
        }

        SettingsClient client = uCoreAdapter.getAppFrameworkClient(SettingsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);

        try {
            UCoreSettings settings = client.getSettings(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(), uCoreAdapter.API_VERSION);
            Settings appSettings = settingsConverter.convertUcoreToAppSettings(settings);
            eventing.post(new SettingsBackendSaveResponse(appSettings));
        } catch (RetrofitError retrofitError) {
            eventing.post(new BackendDataRequestFailed(retrofitError));
        }

    }

    private void postError(int referenceId, final RetrofitError error) {
        eventing.post(new BackendResponse(referenceId, error));
    }

    public void sendToBackend(SettingsBackendSaveRequest event) {
        if (isUserInvalid()) {
            postError(event.getEventId(), getNonLoggedInError());
            return;
        }
        if (uCoreAccessProvider == null) {
            return;
        }
        Settings settings = event.getSettings();

        try {
            UCoreSettings uCoreSettings = settingsConverter.convertAppToUcoreSettings(settings);
            SettingsClient appFrameworkClient = uCoreAdapter.getAppFrameworkClient(SettingsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
            Response response = appFrameworkClient.updateSettings(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(), uCoreSettings);
            eventing.post(new SyncBitUpdateRequest(OrmTableType.SETTINGS, true));

            if (isResponseSuccess(response)) {
                Log.d(getClass().getName(), "Response is :==" + isResponseSuccess(response));
            }

        } catch (RetrofitError retrofitError) {
            eventing.post(new BackendDataRequestFailed(retrofitError));
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
}