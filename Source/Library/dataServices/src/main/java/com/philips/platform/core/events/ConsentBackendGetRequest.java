package com.philips.platform.core.events;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentBackendGetRequest extends Event {

    private final int referenceId;

    public ConsentBackendGetRequest(int referenceId) {
        this.referenceId = referenceId;
    }

    public int getReferenceId() {
        return referenceId;
    }
}
