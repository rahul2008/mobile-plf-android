package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.OrmTableType;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

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
        DataServicesManager.getInstance().getAppComponant().injectConsentsMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ConsentBackendSaveRequest event) {
        if (event.getRequestType() == ConsentBackendSaveRequest.RequestType.SAVE) {
            consentDataSender.sendDataToBackend(event.getConsentDetailList());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ConsentBackendGetRequest event) {
        consentsDataFetcher.getConsent(event);
    }


}