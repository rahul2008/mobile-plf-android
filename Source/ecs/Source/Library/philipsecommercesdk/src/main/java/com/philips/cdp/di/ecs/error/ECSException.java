package com.philips.cdp.di.ecs.error;

public class ECSException extends Exception {

    private final  String message;

    private final int errorCode;

    @Override
    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ECSException(String message, int errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
}
