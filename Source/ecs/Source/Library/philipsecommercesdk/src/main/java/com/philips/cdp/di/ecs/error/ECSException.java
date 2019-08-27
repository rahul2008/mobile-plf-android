package com.philips.cdp.di.ecs.error;

public class ECSException  {

    private final  String message;

    private final int errorCode;

    public String getMessage() {
        return message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public ECSException(String message, int errorCode) {

        this.message = message;
        this.errorCode = errorCode;
    }
}
