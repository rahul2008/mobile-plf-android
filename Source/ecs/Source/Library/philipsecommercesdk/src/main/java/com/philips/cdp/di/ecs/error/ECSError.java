package com.philips.cdp.di.ecs.error;

public class ECSError {

    private  Exception exception;
    private final String errorMessage;
    private final int errorcode;

    public ECSError(String errorMessage, int errorcode) {

        this.errorMessage = errorMessage;
        this.errorcode = errorcode;
    }

    public Exception getException() {
        return new Exception(errorMessage);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorcode() {
        return errorcode;
    }
}
