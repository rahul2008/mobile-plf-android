package com.philips.cdp.di.ecs.error;

public class ECSError {
    private final String componentID="ecs";
    private  Exception exception;
    private final int errorcode;


    public ECSError(Exception exception, int errorcode) {

        this.exception = exception;
        this.errorcode = errorcode;
    }

    public Exception getException() {
        return exception;
    }


    public int getErrorcode() {
        return errorcode;
    }

    public String getComponentID() {
        return componentID;
    }
}
