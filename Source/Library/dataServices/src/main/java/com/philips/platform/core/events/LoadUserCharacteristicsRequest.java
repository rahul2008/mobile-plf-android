/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBFetchRequestListner;

public class LoadUserCharacteristicsRequest extends Event {

    private final DBFetchRequestListner dbFetchRequestListner;

    public DBFetchRequestListner getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    public LoadUserCharacteristicsRequest(DBFetchRequestListner dbRequestListener) {

        this.dbFetchRequestListner = dbRequestListener;
    }
}
