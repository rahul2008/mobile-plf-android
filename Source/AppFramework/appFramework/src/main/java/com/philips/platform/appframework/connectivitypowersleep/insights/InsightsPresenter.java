/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.insights;


import android.content.Context;

import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

public class InsightsPresenter implements InsightsContract.Action, DBFetchRequestListner {

    private InsightsContract.View view;

    private Context context;

    private static final String TAG = InsightsPresenter.class.getSimpleName();

    public InsightsPresenter(InsightsContract.View view, Context appContext) {
        this.view=view;
        context=appContext;
    }

    @Override
    public void showArticle(String insightTitle) {

    }

    @Override
    public void loadInsights(DataServicesManager dataServicesManager) {
        view.showProgressBar();
        dataServicesManager.fetchInsights(this);
    }


    @Override
    public void onFetchSuccess(List list) {
        RALog.d(TAG, "onFetchSuccess : " + list.size());
        view.hideProgressBar();
        view.onInsightLoadSuccess(list);
    }

    @Override
    public void onFetchFailure(Exception e) {
        RALog.d(TAG, "onFetchFailure : " + e.getMessage());
        view.hideProgressBar();
        view.onInsightLoadError(e.getMessage());
    }
}
