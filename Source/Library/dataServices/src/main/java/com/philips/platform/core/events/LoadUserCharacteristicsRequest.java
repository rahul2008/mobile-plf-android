/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.listeners.DBFetchRequestListner;

public class LoadUserCharacteristicsRequest extends Event {

    private final DBFetchRequestListner<Characteristics> dbFetchRequestListner;

    public DBFetchRequestListner<Characteristics> getDbFetchRequestListner() {
        return dbFetchRequestListner;
    }

    public LoadUserCharacteristicsRequest(DBFetchRequestListner<Characteristics> dbRequestListener) {

        this.dbFetchRequestListner = dbRequestListener;
    }
}
