package com.philips.platform.core.events;

import com.philips.platform.datasync.insights.UCoreInsight;

import java.util.List;

public class InsightBackendDeleteRequest extends Event {
    private final List<UCoreInsight> UCoreInsightList;

    public List<UCoreInsight> getUCoreInsightList() {
        return UCoreInsightList;
    }

    public InsightBackendDeleteRequest(List<UCoreInsight> UCoreInsightList) {
        this.UCoreInsightList = UCoreInsightList;
    }
}
