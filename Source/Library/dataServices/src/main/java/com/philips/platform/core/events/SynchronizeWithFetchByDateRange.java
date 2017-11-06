package com.philips.platform.core.events;

import com.philips.platform.core.listeners.SynchronisationCompleteListener;

public class SynchronizeWithFetchByDateRange extends Event {

    private String startDate;
    private String endDate;
    private SynchronisationCompleteListener synchronisationCompleteListener;

    public SynchronizeWithFetchByDateRange(String startDate, String endDate, SynchronisationCompleteListener synchronisationCompleteListener) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.synchronisationCompleteListener = synchronisationCompleteListener;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public SynchronisationCompleteListener getSynchronisationCompleteListener() {
        return synchronisationCompleteListener;
    }
}
