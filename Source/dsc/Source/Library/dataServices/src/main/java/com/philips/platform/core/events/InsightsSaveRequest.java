package com.philips.platform.core.events;

import com.philips.platform.core.datatypes.Insight;

import java.util.Collections;
import java.util.List;

public class InsightsSaveRequest extends Event {
    public List<Insight> insights;

    public InsightsSaveRequest() {
        this.insights = Collections.emptyList();
    }
}
