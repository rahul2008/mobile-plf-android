/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class DeleteAllInsights extends Event {
    final DBRequestListener<Insight> dbRequestListener;

    public DeleteAllInsights(DBRequestListener<Insight> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<Insight> getDbRequestListener() {
        return dbRequestListener;
    }
}
