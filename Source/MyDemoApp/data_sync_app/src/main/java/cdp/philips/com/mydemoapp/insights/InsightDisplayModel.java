package cdp.philips.com.mydemoapp.insights;

public class InsightDisplayModel {

    private String lastModified;
    private String timeStamp;
    private String ruleID;
    private String momentType;

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRuleID() {
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }

    public String getMomentType() {
        return momentType;
    }

    public void setMomentType(String momentType) {
        this.momentType = momentType;
    }
}
