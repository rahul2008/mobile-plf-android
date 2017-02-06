/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Characteristics;

import java.util.List;

public class CharacteristicsBackendSaveRequest extends Event {

    private List<Characteristics> characteristicsList;

    private RequestType requestType = RequestType.SAVE;

    public enum RequestType {SAVE, UPDATE};

    public CharacteristicsBackendSaveRequest(RequestType requestType, List<Characteristics> characteristicsList) {
        this.requestType = requestType;
        this.characteristicsList = characteristicsList;
    }

    public List<Characteristics> getCharacteristicsList() {
        return characteristicsList;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
