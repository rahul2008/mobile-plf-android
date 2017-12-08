package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentsMonitor extends EventMonitor {

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @NonNull
    private final ConsentDataSender consentDataSender;

    @NonNull
    private final ConsentsDataFetcher consentsDataFetcher;


    @Inject
    public ConsentsMonitor(@NonNull ConsentDataSender consentDataSender, @NonNull ConsentsDataFetcher consentsDataFetcher) {
        this.consentDataSender = consentDataSender;
        this.consentsDataFetcher = consentsDataFetcher;
        DataServicesManager.getInstance().getAppComponent().injectConsentsMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ConsentBackendSaveRequest event) {
        if (event.getRequestType() == ConsentBackendSaveRequest.RequestType.SAVE) {
            consentDataSender.sendDataToBackend(event.getConsentDetailList());
        }
    }
}