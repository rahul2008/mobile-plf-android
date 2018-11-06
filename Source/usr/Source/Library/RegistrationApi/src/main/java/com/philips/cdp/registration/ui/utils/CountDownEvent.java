package com.philips.cdp.registration.ui.utils;

public class CountDownEvent {
    private String event;
    private long timeleft;

    public CountDownEvent(String event, long timeleft) {
        this.event = event;
        this.timeleft = timeleft;
    }

    public String getEvent() {
        return event;
    }

    public long getTimeleft() {
        return timeleft;
    }

}
