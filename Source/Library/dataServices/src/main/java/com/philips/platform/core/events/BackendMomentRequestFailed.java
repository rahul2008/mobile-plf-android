package com.philips.platform.core.events;

import retrofit.RetrofitError;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BackendMomentRequestFailed extends Event {
    RetrofitError exception;

    public BackendMomentRequestFailed(RetrofitError e) {
        super();
        exception = e;
    }

    public RetrofitError getException() {
        return exception;
    }

}
