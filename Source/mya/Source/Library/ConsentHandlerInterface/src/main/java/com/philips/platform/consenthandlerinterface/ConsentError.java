package com.philips.platform.consenthandlerinterface;

public class ConsentError {
    private final String error;
    private final int errorCode;

    public ConsentError(String error, int errorCode) {
        this.error = error;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
