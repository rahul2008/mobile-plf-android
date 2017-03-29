package com.philips.platform.datasync.insights;

import java.util.List;

public class UCoreInsightList {
    private String syncurl;
    private List<UCoreInsight> insights;

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }


    public List<UCoreInsight> getInsights() {
        return insights;
    }

    public void setInsights(List<UCoreInsight> insights) {
        this.insights = insights;
    }
}






