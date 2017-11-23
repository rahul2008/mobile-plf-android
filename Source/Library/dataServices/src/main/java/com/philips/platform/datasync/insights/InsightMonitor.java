/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.FetchInsightRequest;
import com.philips.platform.core.events.DeleteInsightRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class InsightMonitor extends EventMonitor {

    @Inject
    UCoreAccessProvider uCoreAccessProvider;

    @NonNull
    private final InsightDataSender insightDataSender;

    @NonNull
    private final InsightDataFetcher insightDataFetcher;


    @Inject
    public InsightMonitor(@NonNull InsightDataSender insightDataSender, @NonNull InsightDataFetcher insightDataFetcher) {
        this.insightDataSender  =  insightDataSender;
        this.insightDataFetcher = insightDataFetcher;
        DataServicesManager.getInstance().getAppComponent().injectInsightMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(DeleteInsightRequest event) {
        insightDataSender.sendDataToBackend(event.getInsights());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(FetchInsightRequest event) {
       insightDataFetcher.fetchData();
    }
}