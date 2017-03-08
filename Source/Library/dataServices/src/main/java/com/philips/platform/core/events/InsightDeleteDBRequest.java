package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class InsightDeleteDBRequest extends Event {
    private final List<? extends Insight> insights;
    final DBRequestListener dbRequestListener;


    public InsightDeleteDBRequest(List<? extends Insight> insights, DBRequestListener dbRequestListener) {
        this.insights = insights;
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public List<? extends Insight> getInsights() {
        return insights;
    }
}
