package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.ConsentDetail;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendSaveRequest extends Event {

    private final List<ConsentDetail> consentDetailList;
    private RequestType requestType = RequestType.SAVE;

    public enum RequestType {SAVE, UPDATE};

    public ConsentBackendSaveRequest(List<ConsentDetail> consentDetailList, RequestType requestType) {
        this.consentDetailList = consentDetailList;
        this.requestType = requestType;
    }

    public List<ConsentDetail> getConsentDetailList() {
        return consentDetailList;
    }

    public RequestType getRequestType() {
        return requestType;
    }
}
