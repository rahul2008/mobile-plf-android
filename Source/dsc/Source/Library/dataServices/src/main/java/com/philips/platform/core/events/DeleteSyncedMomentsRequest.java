package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

public class DeleteSyncedMomentsRequest extends Event{
    private final DBRequestListener<Moment> dbRequestListener;
    public DeleteSyncedMomentsRequest(DBRequestListener<Moment> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<Moment> getDbRequestListener() {
        return dbRequestListener;
    }
}
