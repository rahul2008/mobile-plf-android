package com.philips.platform.pif.chi.datamodel;

public class ConsentState {
    ConsentStatus consentStatus;
    int version;

    public ConsentState(ConsentStatus consentStatus, int version) {
        this.consentStatus = consentStatus;
        this.version = version;
    }

    public ConsentStatus getConsentStatus() {
        return consentStatus;
    }

    public int getVersion() {
        return version;
    }

}
