package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
public class DeleteExpiredInsightRequest extends Event{
    private final DBRequestListener<Insight> dbRequestListener;

    public DeleteExpiredInsightRequest(DBRequestListener<Insight> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<Insight> getDbRequestListener() {
        return dbRequestListener;
    }
}
