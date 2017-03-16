package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

public class InsightBackendDeleteResponse extends Event {

    private Insight insight;
    private DBRequestListener mDBRequestListener;

    public InsightBackendDeleteResponse(Insight insight, DBRequestListener dbRequestListener) {
        this.insight = insight;
        this.mDBRequestListener = dbRequestListener;
    }

    public Insight getInsight() {
        return insight;
    }

    public DBRequestListener getDBRequestListener() {
        return mDBRequestListener;
    }
}
