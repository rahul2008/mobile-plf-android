package com.philips.platform.pif.chi.datamodel;

import java.util.Date;
import java.util.Objects;

public class ConsentStatus {
    private ConsentStates consentState;
    private int version;
    private final Date lastModifiedTimeStamp;

    public ConsentStatus(ConsentStates consentState, int version, Date lastModifiedTimeStamp) {
        this.consentState = consentState;
        this.version = version;
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
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
    }

    @Override
    public int hashCode() {
        return Objects.hash(consentState, version);

    }

    public Date getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }
}
