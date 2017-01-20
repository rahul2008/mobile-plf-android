/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.UserCharacteristics;

public class CharacteristicsBackendSaveRequest extends Event {

    private UserCharacteristics characteristic;

    private RequestType requestType = RequestType.SAVE;

    public enum RequestType {SAVE, UPDATE};

    public CharacteristicsBackendSaveRequest(RequestType requestType, UserCharacteristics characteristic) {
        this.requestType = requestType;
        this.characteristic = characteristic;
    }

    public UserCharacteristics getCharacteristic() {
        return characteristic;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
