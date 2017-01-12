/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.listeners.DBRequestListener;

public class UCDBUpdateFromBackendRequest extends Event {

    private Characteristics characteristics;
    private final DBRequestListener dbRequestListener;

    public DBRequestListener getDbRequestListener() {
        return dbRequestListener;
    }

    public UCDBUpdateFromBackendRequest(Characteristics characteristics, DBRequestListener dbRequestListener) {
        this.characteristics = characteristics;
        this.dbRequestListener = dbRequestListener;

    }

    public Characteristics getCharacteristics() {
        return characteristics;
    }
}
