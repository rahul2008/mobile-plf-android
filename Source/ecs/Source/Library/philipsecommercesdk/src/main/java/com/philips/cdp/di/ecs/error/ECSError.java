package com.philips.cdp.di.ecs.error;

public class ECSError {

    private final Exception exception;
    private final String errorMessage;
    private final int errorcode;

    public ECSError(Exception exception, String errorMessage, int errorcode) {
        this.exception = exception;
        this.errorMessage = errorMessage;
        this.errorcode = errorcode;
    }

    public Exception getException() {
        return exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorcode() {
        return errorcode;
    }
}
