package com.philips.platform.datasync.insights;

import java.util.List;

public class UCoreInsightList {
    private List<Insight> insights;
    private String syncurl;

    public void setInsights(List<Insight> insights) {
        this.insights = insights;
    }

    public List<Insight> getInsights() {
        return insights;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }
}






