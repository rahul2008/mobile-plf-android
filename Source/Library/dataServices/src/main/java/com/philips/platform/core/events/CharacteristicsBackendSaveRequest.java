package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Characteristics;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CharacteristicsBackendSaveRequest extends Event {

    private Characteristics characteristic;

    private RequestType requestType = RequestType.SAVE;

    public enum RequestType {SAVE, UPDATE}

    ;

    public CharacteristicsBackendSaveRequest(RequestType requestType, Characteristics characteristic) {
        this.requestType = requestType;
        this.characteristic = characteristic;
    }

    public Characteristics getCharacteristic() {
        return characteristic;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
