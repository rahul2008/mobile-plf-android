package com.philips.platform.core.events;

import com.philips.platform.datasync.insights.Insight;

import java.util.List;

public class InsightBackendDeleteRequest extends Event {
    private final List<Insight> insightList;

    public List<Insight> getInsightList() {
        return insightList;
    }

    public InsightBackendDeleteRequest(List<Insight> insightList) {
        this.insightList = insightList;
    }
}
