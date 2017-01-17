/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.listeners.DBRequestListener;

public class UserCharacteristicsSaveRequest extends Event {

    private UserCharacteristics userCharacteristics;
    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public UserCharacteristicsSaveRequest(UserCharacteristics userCharacteristics, DBRequestListener dbRequestListener) {
        this.userCharacteristics = userCharacteristics;
        this.dbRequestListener = dbRequestListener;

    }

    public UserCharacteristics getUserCharacteristics() {
        return userCharacteristics;
    }
}
