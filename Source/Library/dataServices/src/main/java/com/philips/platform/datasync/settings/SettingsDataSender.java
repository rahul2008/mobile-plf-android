package com.philips.platform.datasync.settings;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SettingsDataSender extends EventMonitor implements DataSender<Settings> {

    @Inject
    Eventing eventing;

    @NonNull
    final AtomicInteger synchronizationState = new AtomicInteger(0);

    @Inject
    public SettingsDataSender() {
        DataServicesManager.getInstance().mAppComponent.injectSettingsDataSender(this);
    }

    @Override
    public boolean sendDataToBackend(@NonNull List<? extends Settings> dataToSend) {

        if (dataToSend!=null && !dataToSend.isEmpty() && synchronizationState.get() != State.BUSY.getCode()) {
            eventing.post(new SettingsBackendSaveRequest((List<Settings>) dataToSend));
        }

        return false;
    }

    @Override
    public Class<? extends Settings> getClassForSyncData() {
        return Settings.class;
    }
}