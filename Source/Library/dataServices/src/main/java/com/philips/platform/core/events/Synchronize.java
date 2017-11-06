package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SynchronisationCompleteListener;

public class Synchronize extends Event {

    private SynchronisationCompleteListener synchronisationCompleteListener;
    private String startDate;
    private String endDate;

    public Synchronize(SynchronisationCompleteListener synchronisationCompleteListener) {
        this.synchronisationCompleteListener = synchronisationCompleteListener;
    }

    public Synchronize(SynchronisationCompleteListener synchronisationCompleteListener, String startDate, String endDate) {
        this.synchronisationCompleteListener = synchronisationCompleteListener;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public SynchronisationCompleteListener getSynchronisationCompleteListener() {
        return synchronisationCompleteListener;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
