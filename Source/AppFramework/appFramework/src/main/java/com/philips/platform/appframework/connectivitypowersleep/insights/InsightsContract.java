/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.insights;


import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.List;

public interface InsightsContract {

    interface View{
        void showProgressBar();

        void hideProgressBar();

        void showToast(String message);

        void onInsightLoadSuccess(List<Insight> insightList);

        void onInsightLoadError(String errorMessage);
    }

    interface Action{
        void showArticle(ContentArticle contentArticle);

        void loadInsights(DataServicesManager dataServicesManager);
    }
}
