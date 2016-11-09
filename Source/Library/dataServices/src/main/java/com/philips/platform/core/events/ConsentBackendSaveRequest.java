package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendSaveRequest extends Event {

    private Consent consent;

    private RequestType requestType = RequestType.SAVE;

    public enum RequestType {SAVE, UPDATE}

    ;

    public ConsentBackendSaveRequest(RequestType requestType, Consent consent) {
        this.requestType = requestType;
        this.consent = consent;
    }

    public Consent getConsent() {
        return consent;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
