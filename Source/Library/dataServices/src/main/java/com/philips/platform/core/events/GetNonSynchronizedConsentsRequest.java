package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetNonSynchronizedConsentsRequest extends Event {

    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public GetNonSynchronizedConsentsRequest(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }
}
