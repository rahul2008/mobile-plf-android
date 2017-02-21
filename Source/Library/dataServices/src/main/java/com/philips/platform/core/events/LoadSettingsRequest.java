package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBFetchRequestListner;

/**
 * Created by sangamesh on 13/01/17.
 */
public class LoadSettingsRequest extends Event {

    private final DBFetchRequestListner dbFetchRequestListner;

    public DBFetchRequestListner getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    public LoadSettingsRequest(DBFetchRequestListner dbRequestListener) {

        this.dbFetchRequestListner = dbRequestListener;
    }
}