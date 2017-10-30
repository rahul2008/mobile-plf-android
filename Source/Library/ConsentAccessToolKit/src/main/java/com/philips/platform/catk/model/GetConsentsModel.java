package com.philips.platform.catk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.philips.platform.catk.response.ConsentStatus;

/**
 * Created by Maqsood on 10/13/17.
 */

public class GetConsentsModel {

    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("policyRule")
    @Expose
    private String policyRule;
    @SerializedName("resourceType")
    @Expose
    private String resourceType;
    @SerializedName("status")
    @Expose
    private ConsentStatus status;

    @SerializedName("subject")
    @Expose
    private String subject;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPolicyRule() {
        return policyRule;
    }

    public void setPolicyRule(String policyRule) {
        this.policyRule = policyRule;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public void setStatus(ConsentStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
