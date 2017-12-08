package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

public class DeleteInsightResponse extends Event {

    private Insight insight;
    private DBRequestListener<Insight> mDBRequestListener;

    public DeleteInsightResponse(Insight insight, DBRequestListener<Insight> dbRequestListener) {
        this.insight = insight;
        this.mDBRequestListener = dbRequestListener;
    }

    public Insight getInsight() {
        return insight;
    }

    public DBRequestListener<Insight> getDBRequestListener() {
        return mDBRequestListener;
    }
}
