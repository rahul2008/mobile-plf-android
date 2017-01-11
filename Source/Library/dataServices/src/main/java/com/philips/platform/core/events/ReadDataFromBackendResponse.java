package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ReadDataFromBackendResponse extends Event {
    private final  int referenceId;
    private final DBRequestListener dbRequestListener;
    public ReadDataFromBackendResponse(int referenceId, DBRequestListener dbRequestListener) {

        this.referenceId = referenceId;
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }
}
