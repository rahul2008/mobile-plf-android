package com.philips.platform.core.events;
import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetNonSynchronizedMomentsResponse extends Event {

    private List<? extends Moment> nonSynchronizedMoments;
    private List<? extends Consent> consentDetails;

    public GetNonSynchronizedMomentsResponse(List<? extends Moment> nonSynchronizedMoments,List<? extends Consent> consentDetails) {
        this.nonSynchronizedMoments = nonSynchronizedMoments;
        this.consentDetails=consentDetails;
    }

    public List<? extends Consent> getConsentDetails() {
        return consentDetails;
    }

    public List<? extends Moment> getNonSynchronizedMoments() {
        return nonSynchronizedMoments;
    }
}
