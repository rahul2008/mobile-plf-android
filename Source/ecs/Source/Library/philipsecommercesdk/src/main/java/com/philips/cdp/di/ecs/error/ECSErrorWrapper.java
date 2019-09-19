package com.philips.cdp.di.ecs.error;

public class ECSErrorWrapper  {

    Exception exception;
    ECSError ecsError;

    public ECSErrorWrapper(Exception exception, ECSError ecsError) {
        this.exception = exception;
        this.ecsError = ecsError;
    }

    public Exception getException() {
        return exception;
    }

    public ECSError getEcsError() {
        return ecsError;
    }




}
