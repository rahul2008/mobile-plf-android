package com.philips.platform.datasync.insights;

import java.util.List;

public class UCoreInsightList {
    private List<UCoreInsight> UCoreInsights;
    private String syncurl;

    public void setUCoreInsights(List<UCoreInsight> UCoreInsights) {
        this.UCoreInsights = UCoreInsights;
    }

    public List<UCoreInsight> getUCoreInsights() {
        return UCoreInsights;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }
}






