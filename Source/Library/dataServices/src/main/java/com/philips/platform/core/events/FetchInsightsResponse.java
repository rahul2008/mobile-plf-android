/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class FetchInsightsResponse extends Event {

    private final List<Insight> insights;
    final DBRequestListener<Insight> dbRequestListener;

    public FetchInsightsResponse(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) {
        this.insights = insights;
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<Insight> getDbRequestListener() {
        return dbRequestListener;
    }

    public List<Insight> getInsights() {
        return insights;
    }
}
