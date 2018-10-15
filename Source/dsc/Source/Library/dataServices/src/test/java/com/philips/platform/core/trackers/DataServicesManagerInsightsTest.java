package com.philips.platform.core.trackers;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.verticals.VerticalCreater;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataServicesManagerInsightsTest {
    @Test
    public void createInsight() {
        DataServicesManager manager = DataServicesManager.getInstance();
        manager.dataCreator = new VerticalCreater();

        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertEquals("INSIGHT_TYPE", insight.getType());
    }
}
