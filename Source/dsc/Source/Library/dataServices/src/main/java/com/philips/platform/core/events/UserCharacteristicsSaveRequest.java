/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

public class UserCharacteristicsSaveRequest extends Event {

    private List<Characteristics> characteristicsList;
    private final DBRequestListener<Characteristics> dbRequestListener;

    public DBRequestListener<Characteristics> getDbRequestListener() {
        return dbRequestListener;
    }

    public UserCharacteristicsSaveRequest(List<Characteristics> characteristicsList, DBRequestListener<Characteristics> dbRequestListener) {
        this.characteristicsList = characteristicsList;
        this.dbRequestListener = dbRequestListener;

    }

    public List<Characteristics> getUserCharacteristicsList() {
        return characteristicsList;
    }
}
