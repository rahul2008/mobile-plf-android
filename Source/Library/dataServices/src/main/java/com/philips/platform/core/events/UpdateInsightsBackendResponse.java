package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Settings;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UpdateInsightsBackendResponse extends Event {

   private final List<? extends Insight> insights;

    public List<? extends Insight> getInsights() {
        return insights;
    }

    public UpdateInsightsBackendResponse(List<? extends Insight> insights) {
        this.insights = insights;

    }
}
