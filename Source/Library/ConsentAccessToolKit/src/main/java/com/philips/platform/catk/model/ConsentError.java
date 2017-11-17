package com.philips.platform.catk.model;

public class ConsentError extends Consent {
    private String message;

    public ConsentError(final String message) {
        super(null, null, null, -1);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
