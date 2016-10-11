package com.philips.platform.core.events;
import com.philips.platform.core.datatypes.Moment;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetNonSynchronizedMomentsResponse extends Event {

    private List<? extends Moment> nonSynchronizedMoments;

    public GetNonSynchronizedMomentsResponse(List<? extends Moment> nonSynchronizedMoments) {
        this.nonSynchronizedMoments = nonSynchronizedMoments;
    }

    public List<? extends Moment> getNonSynchronizedMoments() {
        return nonSynchronizedMoments;
    }
}
