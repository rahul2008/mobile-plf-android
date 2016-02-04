package com.philips.dhpclient;

public class DhpCommunicationException extends RuntimeException {

    public DhpCommunicationException(Throwable e) {
        super(e);
    }

    public DhpCommunicationException(String dhpResponseCode, String message) {
        super("DHP responded with code: " + dhpResponseCode + ", message: " + message);
    }
}
