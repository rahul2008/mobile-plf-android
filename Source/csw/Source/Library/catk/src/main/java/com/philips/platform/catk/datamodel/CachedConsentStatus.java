package com.philips.platform.catk.datamodel;

import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.joda.time.DateTime;

import java.util.Objects;

public class CachedConsentStatus extends ConsentStatus {
    private DateTime expires;

    public CachedConsentStatus(ConsentStates consentState, int version, DateTime expires) {
        super(consentState, version);
        this.expires = expires;
    }


    public DateTime getExpires() {
        return expires;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof CachedConsentStatus)) return false;

        CachedConsentStatus cachedConsentStatus = (CachedConsentStatus) o;
        if (!super.equals(cachedConsentStatus))
            return false;

        return expires != null ? expires.equals(cachedConsentStatus.expires) : cachedConsentStatus.expires == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConsentState(), getVersion(), expires);

    }
}
