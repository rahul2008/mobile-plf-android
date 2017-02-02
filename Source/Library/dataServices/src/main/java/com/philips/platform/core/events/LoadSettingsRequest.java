package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;

/**
 * Created by sangamesh on 13/01/17.
 */
public class LoadSettingsRequest extends Event {

    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public LoadSettingsRequest(DBRequestListener dbRequestListener) {

        this.dbRequestListener = dbRequestListener;
    }
}