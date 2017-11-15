/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateConsentModel {

    @SerializedName("resourceType")
    @Expose
    private String resourceType;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("policyRule")
    @Expose
    private String policyRule;

    public CreateConsentModel() {
    }

    public CreateConsentModel(String language, String policyRule, String resourceType, String status, String subject) {
        this.resourceType = resourceType;
        this.language = language;
        this.status = status;
        this.subject = subject;
        this.policyRule = policyRule;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPolicyRule() {
        return policyRule;
    }

    public void setPolicyRule(String policyRule) {
        this.policyRule = policyRule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreateConsentModel)) return false;

        CreateConsentModel that = (CreateConsentModel) o;

        if (resourceType != null ? !resourceType.equals(that.resourceType) : that.resourceType != null)
            return false;
        if (language != null ? !language.equals(that.language) : that.language != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (subject != null ? !subject.equals(that.subject) : that.subject != null) return false;
        return policyRule != null ? policyRule.equals(that.policyRule) : that.policyRule == null;
    }

    @Override
    public int hashCode() {
        int result = resourceType != null ? resourceType.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (subject != null ? subject.hashCode() : 0);
        result = 31 * result + (policyRule != null ? policyRule.hashCode() : 0);
        return result;
    }
}