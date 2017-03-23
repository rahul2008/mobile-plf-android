package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;

/**
 * Created by 310218660 on 2/7/2017.
 */

public class DeleteAllMomentsRequest extends Event{
    private final DBRequestListener<Moment> dbRequestListener;
    public DeleteAllMomentsRequest(DBRequestListener<Moment> dbRequestListener) {
        this.dbRequestListener = dbRequestListener;
    }

    public DBRequestListener<Moment> getDbRequestListener() {
        return dbRequestListener;
    }
}
