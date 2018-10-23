package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class InsightsSaveRequest extends Event {
    private List<Insight> insights;
    private DBRequestListener<Insight> dbListener;

    public InsightsSaveRequest(final List<Insight> insights, final DBRequestListener<Insight> dbListener) {
        this.insights = insights;
        this.dbListener = dbListener;
    }

    public List<Insight> getInsights() {
        return insights;
    }

    public DBRequestListener<Insight> getDbRequestListener() {
        return dbListener;
    }
}
