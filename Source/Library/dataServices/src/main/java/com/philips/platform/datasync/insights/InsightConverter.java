package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

public class InsightConverter {
    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public InsightConverter() {
        DataServicesManager.getInstance().getAppComponant().injectInsightConverter(this);
    }

    @NonNull
    public List<Insight> convertToAppInsights(@NonNull final UCoreInsightList uCoreInsights) {
        List<Insight> insights = new ArrayList<>();
        return insights;
    }

    @NonNull
    public UCoreInsightList convertToUCoreInsights(@NonNull final Collection<? extends Insight> appInsightList) {
        return null;
    }
}