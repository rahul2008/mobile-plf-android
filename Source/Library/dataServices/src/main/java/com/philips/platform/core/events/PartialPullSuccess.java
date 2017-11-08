package com.philips.platform.core.events;

public class PartialPullSuccess extends Event {
    private String tillDate;

    public PartialPullSuccess(String tillDate) {
        this.tillDate = tillDate;
    }

    public String getTillDate() {
        return tillDate;
    }
}
