package com.philips.platform.datasync.insights;

import android.support.annotation.NonNull;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Characteristics;
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

        List<UCoreInsight> uCoreInsightList = uCoreInsights.getUCoreInsights();
        List<Insight> appInsightList = new ArrayList<>();

        for (UCoreInsight uCoreInsight : uCoreInsightList) {
            Insight appInsight = dataCreator.createInsight(uCoreInsight);
            appInsightList.add(appInsight);
        }

        return appInsightList;
    }

    @NonNull
    public UCoreInsightList convertToUCoreInsights(@NonNull final Collection<? extends Insight> appInsightList) {

        UCoreInsightList uCoreInsightList = new UCoreInsightList();

        List<UCoreInsight> uCoreInsights = new ArrayList<>();
        List<Insight> insightList = convertFromCollectionToList(appInsightList);

        for (Insight insight : insightList) {
            UCoreInsight uCoreInsight = new UCoreInsight();
            uCoreInsight.setGuid(insight.getGUId());
            uCoreInsight.setLastModified(insight.getLastModified());
            uCoreInsight.setInactive(insight.isInactive());
            uCoreInsight.setVersion(insight.getVersion());
            uCoreInsight.setRuleId(insight.getRuleId());
            uCoreInsight.setSubjectId(insight.getSubjectId());
            uCoreInsight.setMomentId(insight.getMomentId());
            uCoreInsight.setType(insight.getType());
            uCoreInsight.setTimeStamp(insight.getTimeStamp());
            uCoreInsight.setTitle(insight.getTitle());
            uCoreInsight.setProgram_maxversion(insight.getProgram_maxVersion());
            uCoreInsight.setProgram_minversion(insight.getProgram_minVersion());

            UCoreMetaData metaData = new UCoreMetaData();
            metaData.setAvg(insight.getMetadataAvg());
            metaData.setMax(insight.getMetadataMax());
            metaData.setMin(insight.getMetadataMin());

            uCoreInsight.setMetadata(metaData);
            uCoreInsights.add(uCoreInsight);
        }

        uCoreInsightList.setUCoreInsights(uCoreInsights);
        return uCoreInsightList;
    }

    private List<Insight> convertFromCollectionToList(Collection<? extends Insight> insights) {
        List<Insight> insightList = new ArrayList<>();
        for (Insight insight : insightList) {
            insightList.add(insight);
        }
        return insightList;
    }
}