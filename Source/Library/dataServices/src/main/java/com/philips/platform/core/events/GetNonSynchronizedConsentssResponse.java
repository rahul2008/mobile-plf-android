package com.philips.platform.core.events;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetNonSynchronizedConsentssResponse extends Event {

    private List<ConsentDetail> consentDetails;

    public GetNonSynchronizedConsentssResponse(List<ConsentDetail> consentDetails) {
        this.consentDetails=consentDetails;
    }

    public List<? extends ConsentDetail> getConsentDetails() {
        return consentDetails;
    }

}
