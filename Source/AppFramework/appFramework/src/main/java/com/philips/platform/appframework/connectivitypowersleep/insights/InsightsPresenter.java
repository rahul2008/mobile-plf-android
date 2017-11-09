/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.insights;


import android.content.Context;

import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.core.trackers.DataServicesManager;

public class InsightsPresenter implements InsightsContract.Action {

    private InsightsContract.View view;

    private Context context;

    public InsightsPresenter(InsightsContract.View view, Context appContext) {
        this.view=view;
        context=appContext;
    }

    @Override
    public void showArticle(ContentArticle contentArticle) {

    }

    @Override
    public void loadInsights(DataServicesManager dataServicesManager) {

    }


}
