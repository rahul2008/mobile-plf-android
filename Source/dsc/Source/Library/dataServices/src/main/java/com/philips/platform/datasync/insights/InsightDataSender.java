/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.DeleteInsightResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataSender;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

@SuppressWarnings({"rawtypes", "unchecked"})
public class InsightDataSender extends DataSender {

    @Inject
    UCoreAccessProvider uCoreAccessProvider;
    @Inject
    Eventing eventing;

    @NonNull
    private final UCoreAdapter uCoreAdapter;
    @NonNull
    private final GsonConverter gsonConverter;
    @NonNull
    private final InsightConverter insightConverter;
    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    protected final Set<Integer> insightIds = new HashSet<>();

    @Inject
    public InsightDataSender(@NonNull UCoreAdapter uCoreAdapter,
                             @NonNull GsonConverter gsonConverter,
                             @NonNull InsightConverter insightConverter) {
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        this.insightConverter = insightConverter;
        DataServicesManager.getInstance().getAppComponent().injectInsightDataSender(this);
    }

    @Override
    public boolean sendDataToBackend(@NonNull final List dataToSend) {
        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            if (!uCoreAccessProvider.isLoggedIn()) {
                return false;
            }

            if (dataToSend != null && !dataToSend.isEmpty()) {
                List<Insight> insightsToSync = new ArrayList<>();
                synchronized (insightIds) {
                    for (Insight insight : (List<Insight>) dataToSend) {
                        if (insightIds.add(insight.getId())) {
                            insightsToSync.add(insight);
                        }
                    }
                }
                return sendInsightsToBackend(insightsToSync);
            }
        }
        return false;
    }

    private boolean sendInsightsToBackend(final List<Insight> insightsToSync) {
        boolean isFailed = false;
        InsightClient client = uCoreAdapter.getAppFrameworkClient(InsightClient.class,
                uCoreAccessProvider.getAccessToken(), gsonConverter);
        if(client==null) return isFailed;
        for (Insight insight : insightsToSync) {
            isFailed = isFailed || deleteInsight(client, insight);

            synchronized (insightIds) {
                insightIds.remove(insight.getId());
            }
        }
        return isFailed;
    }

    private boolean deleteInsight(final InsightClient client, final Insight insight) {
        try {
            Response response = client.deleteInsight(uCoreAccessProvider.getUserId(),
                    insight.getSynchronisationData().getGuid(), uCoreAccessProvider.getUserId());

            if (isResponseSuccess(response)) {
                postDeletedOk(insight);
            }
        } catch (RetrofitError error) {
            onError(error);
            eventing.post(new BackendResponse(1, error));
        }
        return false;
    }

    void postDeletedOk(final Insight insight) {
        eventing.post(new DeleteInsightResponse(insight, null));
    }

    boolean isResponseSuccess(final Response response) {
        return response != null && (response.getStatus() == HttpURLConnection.HTTP_OK || response.getStatus() == HttpURLConnection.HTTP_CREATED
                || response.getStatus() == HttpURLConnection.HTTP_NO_CONTENT);
    }

    @Override
    public Class<? extends Insight> getClassForSyncData() {
        return Insight.class;
    }
}