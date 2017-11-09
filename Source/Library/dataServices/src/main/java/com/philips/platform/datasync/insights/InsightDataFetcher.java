/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.FetchInsightsResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class InsightDataFetcher extends DataFetcher {
    public static final String TAG = "InsightDataFetcher";

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

    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

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
    public RetrofitError fetchData() {

        if(synchronizationState.get() != DataSender.State.BUSY.getCode()) {

            if (isUserInvalid()) {
                postError(1, getNonLoggedInError());
            } else {
                getInsights();
            }
        }
        return null;
    }

    @Override
    public RetrofitError fetchDataByDateRange(String startDate, String endDate) {
        return null;
    }

    @Override
    public RetrofitError fetchAllData() {
        return super.fetchAllData();
    }

    public void getInsights() {

        InsightClient client = uCoreAdapter.getAppFrameworkClient(InsightClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
        if(client==null) return;
        try {
            UCoreInsightList insightList = client.fetchInsights(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(),
                    UCoreAdapter.API_VERSION, uCoreAccessProvider.getInsightLastSyncTimestamp());

            uCoreAccessProvider.saveLastSyncTimeStamp(insightList.getSyncurl(), UCoreAccessProvider.INSIGHT_LAST_SYNC_URL_KEY);

            List<Insight> insights = insightConverter.convertToAppInsights(insightList);
            if(insights==null || insights.size()==0)return;
            eventing.post(new FetchInsightsResponse(insights, null));
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

    void postError(int referenceId, final RetrofitError error) {
        eventing.post(new BackendResponse(referenceId, error));
    }

    RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }
}

