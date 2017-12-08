/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class UCDBUpdateFromBackendRequest extends Event {

    private List<Characteristics> characteristicsList;
    private final DBRequestListener<Characteristics> dbRequestListener;

    public DBRequestListener<Characteristics> getDbRequestListener() {
        return dbRequestListener;
    }

    public UCDBUpdateFromBackendRequest(List<Characteristics> characteristicsList, DBRequestListener<Characteristics> dbRequestListener) {
        this.characteristicsList = characteristicsList;
        this.dbRequestListener = dbRequestListener;

    }

    public List<Characteristics> getUserCharacteristics() {
        return characteristicsList;
    }
}
