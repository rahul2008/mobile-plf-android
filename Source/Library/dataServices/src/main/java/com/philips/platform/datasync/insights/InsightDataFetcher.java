/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.consent.ConsentsClient;
import com.philips.platform.datasync.consent.ConsentsConverter;
import com.philips.platform.datasync.consent.UCoreConsentDetail;
import com.philips.platform.datasync.settings.SettingsClient;
import com.philips.platform.datasync.settings.UCoreSettings;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class InsightDataFetcher extends DataFetcher {
    public static final String TAG = "InsightDataFetcher";
    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    @Inject
    Eventing eventing;

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @Inject
    UCoreAccessProvider uCoreAccessProvider;


    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final InsightConverter insightConverter;


    @Inject
    public InsightDataFetcher(@NonNull UCoreAdapter uCoreAdapter, @NonNull GsonConverter gsonConverter, @NonNull InsightConverter insightConverter) {
        super(uCoreAdapter);
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        this.insightConverter = insightConverter;
        DataServicesManager.getInstance().getAppComponant().injectInsightsDataFetcher(this);
    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        getInsights();
        return null;
    }

    @Override
    public RetrofitError fetchAllData() {

        return super.fetchAllData();
    }


    public void getInsights() {

        if (uCoreAccessProvider == null) {
            return;
        }

        InsightClient client = uCoreAdapter.getAppFrameworkClient(InsightClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);

        try {
            List<UCoreInsight> insightList = client.fetchInsights(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(), UCoreAdapter.API_VERSION, uCoreAccessProvider.getInsightLastSyncTimestamp());

            System.out.println("***InsightList****" + insightList.size());
//            uCoreAccessProvider.saveLastSyncTimeStamp(insightList.getSyncurl(), UCoreAccessProvider.INSIGHT_LAST_SYNC_URL_KEY);
//            Settings appSettings = insightConverter.convertUcoreToAppSettings(settings);
//            if(appSettings==null)return;
//            eventing.post(new SettingsBackendSaveResponse(appSettings));
        } catch (RetrofitError retrofitError) {
//            eventing.post(new BackendDataRequestFailed(retrofitError));
            System.out.println("***InsightList fetch error" + retrofitError.getMessage());
        }
    }

    public boolean isUserInvalid() {
        if (uCoreAccessProvider != null) {
            String accessToken = uCoreAccessProvider.getAccessToken();
            return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
        }
        return false;
    }

    private void postError(int referenceId, final RetrofitError error) {
        DSLog.i(DSLog.LOG, "Error In ConsentsMonitor - posterror");
        eventing.post(new BackendResponse(referenceId, error));
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    public void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }

}

