package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.trackers.DataServicesManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class InsightConverter {

    @Inject
    BaseAppDataCreator dataCreator;

    @Inject
    public InsightConverter() {
        DataServicesManager.getInstance().getAppComponant().injectInsightConverter(this);
    }

    @NonNull
    public List<Insight> convertToAppInsights(@NonNull final List<UCoreInsight> uCoreInsights) {

        List<Insight> insights =new ArrayList<>();


        return insights;
    }

    @NonNull
    public List<UCoreInsight> convertToUCoreInsights(@NonNull final Collection<? extends Insight> insights) {
        List<UCoreInsight> uCoreInsights = new ArrayList<>();

        return uCoreInsights;
    }

   }