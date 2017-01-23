/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.UserCharacteristics;
import com.philips.platform.core.listeners.DBRequestListener;

public class UCUpdateDBSyncBitRequest extends Event {

    private UserCharacteristics userCharacteristics;
    private final boolean isSynced;


    public UCUpdateDBSyncBitRequest(UserCharacteristics userCharacteristics, boolean isSynced) {
        this.userCharacteristics = userCharacteristics;

        this.isSynced = isSynced;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public UserCharacteristics getUserCharacteristics() {
        return userCharacteristics;
    }
}
