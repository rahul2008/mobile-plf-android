package com.philips.platform.appframework.flowmanager.exceptions;

public class NoStateException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public NoStateException() {
        super("No State Found");
    }
}
