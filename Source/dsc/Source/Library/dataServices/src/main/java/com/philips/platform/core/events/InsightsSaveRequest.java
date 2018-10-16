package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;

import java.util.List;

public class InsightsSaveRequest extends Event {
    private List<Insight> insights;

    public InsightsSaveRequest(final List<Insight> insights) {
        this.insights = insights;
    }

    public List<Insight> getInsights() {
        return insights;
    }
}
