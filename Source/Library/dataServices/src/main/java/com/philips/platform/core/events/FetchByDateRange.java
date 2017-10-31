package com.philips.platform.core.events;

public class FetchByDateRange extends Event {

    private String startDate;
    private String endDate;

    public FetchByDateRange(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
