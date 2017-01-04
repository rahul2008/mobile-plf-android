package com.philips.platform.flowmanager.exceptions;

public class NoStateException extends RuntimeException {


    public NoStateException() {
        super("No State Found");
    }
}
