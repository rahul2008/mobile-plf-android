package com.philips.platform.mya.catk.datamodel;

import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.Date;

public class CachedConsentStatus extends ConsentStatus {
    private Date expires;

    public CachedConsentStatus(ConsentStates consentState, int version, Date expires) {
        super(consentState, version);
        this.expires = expires;
    }


    public Date getExpires() {
        return expires;
    }
}
