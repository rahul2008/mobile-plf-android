package com.philips.platform.pif.chi.datamodel;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsentStatus)) return false;

        ConsentStatus consentStatus = (ConsentStatus) o;

        if (version != consentStatus.version) return false;
        if (consentState != null ? !consentState.equals(consentStatus.consentState) : consentStatus.consentState != null)
            return false;
        return true;
        //return timestamp != null ? timestamp.equals(consent.timestamp) : consent.timestamp == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentState, version);

    }

}
