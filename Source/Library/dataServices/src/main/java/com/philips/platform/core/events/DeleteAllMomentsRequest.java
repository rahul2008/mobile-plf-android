package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;

/**
 * Created by 310218660 on 2/7/2017.
 */

public class DeleteAllMomentsRequest extends Event{
    private final DBRequestListener dbRequestListener;
    public DeleteAllMomentsRequest(DBRequestListener dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }
}
