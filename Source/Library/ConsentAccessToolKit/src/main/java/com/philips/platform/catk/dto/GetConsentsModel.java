/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.philips.platform.catk.model.ConsentStatus;

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

    public GetConsentsModel() {
    }
    
    public GetConsentsModel(String dateTime, String language, String policyRule, String resourceType, ConsentStatus status, String subject) {
        this.dateTime = dateTime;
        this.language = language;
        this.policyRule = policyRule;
        this.resourceType = resourceType;
        this.status = status;
        this.subject = subject;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetConsentsModel)) return false;

        GetConsentsModel that = (GetConsentsModel) o;

        if (dateTime != null ? !dateTime.equals(that.dateTime) : that.dateTime != null)
            return false;
        if (language != null ? !language.equals(that.language) : that.language != null)
            return false;
        if (policyRule != null ? !policyRule.equals(that.policyRule) : that.policyRule != null)
            return false;
        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null)
            return false;
        if (status != that.status) return false;
        return subject != null ? subject.equals(that.subject) : that.subject == null;

    }

    @Override
    public int hashCode() {
        int result = dateTime != null ? dateTime.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (policyRule != null ? policyRule.hashCode() : 0);
        result = 31 * result + (resourceType != null ? resourceType.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        return result;
    }
}
