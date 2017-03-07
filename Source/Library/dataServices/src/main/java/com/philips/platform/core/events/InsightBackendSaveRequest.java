package com.philips.platform.core.events;


import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Insight;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class InsightBackendSaveRequest extends Event {

    private final List<Insight> insightList;

    public List<Insight> getInsightList() {
        return insightList;
    }

    public InsightBackendSaveRequest(List<Insight> insightList) {
        this.insightList = insightList;
    }

}
