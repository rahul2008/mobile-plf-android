package com.philips.platform.appframework.flowmanager.exceptions;

public class NoStateException extends RuntimeException {


    private static final long serialVersionUID = 2474977360521233155L;

    public NoStateException() {
        super("No State found with that Id");
    }
}
