/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class DeleteInsightFromDB extends Event {
    private final List<Insight> insights;
    final DBRequestListener dbRequestListener;

    public DeleteInsightFromDB(List<Insight> insights, DBRequestListener dbRequestListener) {
        this.insights = insights;
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public List<Insight> getInsights() {
        return insights;
    }
}
