package com.philips.platform.core.events;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetNonSynchronizedMomentsResponse extends Event {

    private List<Moment> nonSynchronizedMoments;
    private List<ConsentDetail> consentDetails;

    public GetNonSynchronizedMomentsResponse(List<Moment> nonSynchronizedMoments,List<ConsentDetail> consentDetails) {
        this.nonSynchronizedMoments = nonSynchronizedMoments;
        this.consentDetails=consentDetails;
    }

    public List<? extends ConsentDetail> getConsentDetails() {
        return consentDetails;
    }

    public List<? extends Moment> getNonSynchronizedMoments() {
        return nonSynchronizedMoments;
    }
}
