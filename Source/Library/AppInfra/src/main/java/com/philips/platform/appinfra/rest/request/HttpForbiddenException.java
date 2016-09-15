package com.philips.platform.appinfra.rest.request;

/**
 * Created by 310238114 on 9/14/2016.
 */
public class HttpForbiddenException extends Exception{

    private final String message = "http calls are depricated use https calls only";
    public HttpForbiddenException() {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
