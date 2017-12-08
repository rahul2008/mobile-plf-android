package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
public class DeleteExpiredMomentRequest extends Event{
    private final DBRequestListener<Integer> dbRequestListener;

    public DeleteExpiredMomentRequest(DBRequestListener<Integer> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<Integer> getDbRequestListener() {
        return dbRequestListener;
    }
}
