package com.philips.platform.pif.chi.datamodel;

public class ConsentStatus {
    private ConsentStates consentState;
    private int version;

    public ConsentStatus(ConsentStates consentState, int version) {
        this.consentState = consentState;
        this.version = version;
    }

    public ConsentStates getConsentState() {
        return consentState;
    }

    public int getVersion() {
        return version;
    }

}
