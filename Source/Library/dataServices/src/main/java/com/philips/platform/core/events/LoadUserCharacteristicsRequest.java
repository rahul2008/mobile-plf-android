/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.core.events;

import com.philips.platform.core.listeners.DBRequestListener;

public class LoadUserCharacteristicsRequest extends Event {

    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public LoadUserCharacteristicsRequest(DBRequestListener dbRequestListener) {

        this.dbRequestListener = dbRequestListener;
    }
}
