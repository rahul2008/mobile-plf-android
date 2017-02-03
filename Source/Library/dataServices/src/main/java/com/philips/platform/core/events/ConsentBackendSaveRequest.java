package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.Consent;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendSaveRequest extends Event {

    private final List<Consent> consentList;
    private RequestType requestType = RequestType.SAVE;

    public enum RequestType {SAVE, UPDATE};

    public ConsentBackendSaveRequest(List<Consent> consentList, RequestType requestType) {
        this.consentList = consentList;
        this.requestType = requestType;
    }

    public List<Consent> getConsentList() {
        return consentList;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
