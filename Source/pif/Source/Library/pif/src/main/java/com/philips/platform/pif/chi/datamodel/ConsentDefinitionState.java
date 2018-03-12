package com.philips.platform.pif.chi.datamodel;

public class ConsentDefinitionState {
    ConsentStatus consentStatus;
    ConsentVersionStatus consentVersionStatus;
    ConsentDefinition consentDefinition;

    public ConsentStatus getConsentStatus() {
        return consentStatus;
    }

    public void setConsentStatus(ConsentStatus consentStatus) {
        this.consentStatus = consentStatus;
    }

    public ConsentVersionStatus getConsentVersionStatus() {
        return consentVersionStatus;
    }

    public void setConsentVersionStatus(ConsentVersionStatus consentVersionStatus) {
        this.consentVersionStatus = consentVersionStatus;
    }

    public ConsentDefinition getConsentDefinition() {
        return consentDefinition;
    }

    public void setConsentDefinition(ConsentDefinition consentDefinition) {
        this.consentDefinition = consentDefinition;
    }
}
