package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBFetchRequestListner;

/**
 * Created by sangamesh on 13/01/17.
 */
public class LoadSettingsRequest extends Event {

    private final DBFetchRequestListner<Settings> dbFetchRequestListner;

    public DBFetchRequestListner<Settings> getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    public LoadSettingsRequest(DBFetchRequestListner<Settings> dbRequestListener) {

        this.dbFetchRequestListner = dbRequestListener;
    }
}