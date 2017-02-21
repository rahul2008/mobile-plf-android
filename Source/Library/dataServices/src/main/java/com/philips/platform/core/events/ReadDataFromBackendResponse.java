package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBFetchRequestListner;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ReadDataFromBackendResponse extends Event {
    private final DBFetchRequestListner dbFetchRequestListner;

    public DBFetchRequestListner getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    public ReadDataFromBackendResponse(int referenceId, DBFetchRequestListner dbRequestListener) {
        super(referenceId);
        this.dbFetchRequestListner = dbRequestListener;

    }
}
