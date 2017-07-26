package com.philips.platform.dscdemo.insights;

public class InsightDisplayModel {

    private String insightID;
    private String momentID;
    private String lastModified;
    private String ruleID;

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getInsightID() {
        return insightID;
    }

    public void setInsightID(String insightID) {
        this.insightID = insightID;
    }

    public String getMomentID() {
        return momentID;
    }

    public void setMomentID(String momentID) {
        this.momentID = momentID;
    }

    public String getRuleID() {
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }
}
