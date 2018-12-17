package com.philips.themesettings;

public class MessageEvent {
    public MessageEvent(final String message) {
        this.settings = message;
    }

    private String settings;

    public String getMessage() {
        return settings;
    }
}
