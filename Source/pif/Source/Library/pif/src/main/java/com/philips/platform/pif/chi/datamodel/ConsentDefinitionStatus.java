package com.philips.platform.pif.chi.datamodel;

import java.util.Date;

public class ConsentDefinitionStatus {
    private ConsentStates consentState;
    private ConsentVersionStates consentVersionState;
    private ConsentDefinition consentDefinition;
    private Date timestamp;

    public ConsentDefinitionStatus() {
    }

    public ConsentDefinitionStatus(ConsentStates consentState, ConsentVersionStates consentVersionState, ConsentDefinition consentDefinition, Date timestamp) {
        this.consentState = consentState;
        this.consentVersionState = consentVersionState;
        this.consentDefinition = consentDefinition;
        this.timestamp = timestamp;
    }

    public ConsentStates getConsentState() {
        return consentState;
    }

    public void setConsentState(ConsentStates consentState) {
        this.consentState = consentState;
    }

    public ConsentVersionStates getConsentVersionState() {
        return consentVersionState;
    }

    public void setConsentVersionState(ConsentVersionStates consentVersionState) {
        this.consentVersionState = consentVersionState;
    }

    public ConsentDefinition getConsentDefinition() {
        return consentDefinition;
    }

    public void setConsentDefinition(ConsentDefinition consentDefinition) {
        this.consentDefinition = consentDefinition;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
