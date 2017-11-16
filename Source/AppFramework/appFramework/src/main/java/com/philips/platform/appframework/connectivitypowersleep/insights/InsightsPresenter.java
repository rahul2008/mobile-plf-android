/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.insights;


import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.termsandconditions.WebViewEnum;
import com.philips.platform.baseapp.screens.termsandconditions.WebViewStateData;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.Iterator;
import java.util.List;

public class InsightsPresenter implements InsightsContract.Action, DBFetchRequestListner {

    private InsightsContract.View view;

    private Context context;

    private static final String TAG = InsightsPresenter.class.getSimpleName();

    private static final String  INSIGHTS_CLICKED= "article";

    public InsightsPresenter(InsightsContract.View view, Context appContext) {
        this.view=view;
        context=appContext;
    }

    @Override
    public void showArticle(String insightTitle) {
        BaseFlowManager targetFlowManager = ((AppFrameworkApplication)context).getTargetFlowManager();
        BaseState baseState = null;
        try {
            baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.INSIGHTS), INSIGHTS_CLICKED);
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
        }
        if (null != baseState) {
            WebViewStateData webViewStateData =new WebViewStateData();
            if(insightTitle.equalsIgnoreCase("LOW_DEEP_SLEEP")){
                webViewStateData.setWebViewEnum(WebViewEnum.LOW_DEEP_SLEEP_ARTICLE_CLICKED);
            }else{
                webViewStateData.setWebViewEnum(WebViewEnum.HIGH_DEEP_SLEEP_ARTICLE_CLICKED);
            }
            baseState.setUiStateData(webViewStateData);
            baseState.navigate(new FragmentLauncher(((InsightsFragment)view).getActivity(),((InsightsFragment)view).getContainerId(),((InsightsFragment)view).getActionBarListener()));
        }
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
        Iterator iterable = list.iterator();
        while (iterable.hasNext()) {
            Insight insight = (Insight) iterable.next();
            if (!insight.getRuleId().equals("HIGH_DEEP_SLEEP") && !insight.getRuleId().equals("LOW_DEEP_SLEEP")) {
                iterable.remove();
            }
        }
        view.onInsightLoadSuccess(list);
    }

    @Override
    public void onFetchFailure(Exception e) {
        RALog.d(TAG, "onFetchFailure : " + e.getMessage());
        view.hideProgressBar();
        view.onInsightLoadError(e.getMessage());
    }
}
