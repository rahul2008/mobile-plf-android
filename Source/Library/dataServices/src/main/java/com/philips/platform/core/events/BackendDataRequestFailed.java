package com.philips.platform.core.events;

import retrofit.RetrofitError;

public class BackendDataRequestFailed extends Event {
    RetrofitError exception;

    public BackendDataRequestFailed(RetrofitError e) {
        super();
        exception = e;
    }

    public RetrofitError getException() {
        return exception;
    }
}
