package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;

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

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SettingsMonitor extends EventMonitor {

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @NonNull
    private final SettingsDataSender settingsDataSender;

    @NonNull
    private final SettingsDataFetcher settingsDataFetcher;


    @Inject
    public SettingsMonitor(@NonNull SettingsDataSender settingsDataSender, @NonNull SettingsDataFetcher settingsDataFetcher) {
        this.settingsDataSender = settingsDataSender;
        this.settingsDataFetcher = settingsDataFetcher;
        DataServicesManager.getInstance().getAppComponant().injectUserSettingsMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(SettingsBackendSaveRequest event) {
        settingsDataSender.sendToBackend(event);
        System.out.println("In onEventAsync(SettingsBackendSaveRequest event) ");
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(SettingsBackendGetRequest event) {
        settingsDataFetcher.getSettings();
    }



}