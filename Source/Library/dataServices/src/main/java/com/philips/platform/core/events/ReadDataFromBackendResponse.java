package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ReadDataFromBackendResponse extends Event {
    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public ReadDataFromBackendResponse(int referenceId, DBRequestListener dbRequestListener) {
        super(referenceId);
        this.dbRequestListener = dbRequestListener;

    }
}
