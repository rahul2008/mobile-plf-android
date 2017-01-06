package com.philips.platform.appframework.flowmanager.exceptions;

public class NoStateException extends RuntimeException {


    public NoStateException() {
        super("No State Found");
    }
}
