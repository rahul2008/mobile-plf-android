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
import com.philips.platform.core.events.UpdateInsightsBackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.joda.time.DateTime;

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
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            getInsights();
        }
        return null;
    }

    @Override
    public RetrofitError fetchAllData() {

        return super.fetchAllData();
    }


    public void getInsights() {
        if (isUserInvalid())
            postError(1, getNonLoggedInError());

        if (uCoreAccessProvider == null) {
            return;
        }

        InsightClient client = uCoreAdapter.getAppFrameworkClient(InsightClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);

        try {
            UCoreInsightList insightList = client.fetchInsights(uCoreAccessProvider.getUserId(), uCoreAccessProvider.getUserId(),
                    UCoreAdapter.API_VERSION, uCoreAccessProvider.getInsightLastSyncTimestamp());
            List<Insight> insights = insightConverter.convertToAppInsights(insightList);
            eventing.post(new UpdateInsightsBackendResponse(insights, null));
        } catch (RetrofitError retrofitError) {
            eventing.post(new BackendDataRequestFailed(retrofitError));
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
        eventing.post(new BackendResponse(referenceId, error));
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }
}

