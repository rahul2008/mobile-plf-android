package com.philips.platform.core.trackers;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.verticals.VerticalCreater;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataServicesManagerInsightsTest {

    @Test
    public void createInsightWithType() {
        DataServicesManager manager = DataServicesManager.getInstance();
        manager.dataCreator = new VerticalCreater();

        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertEquals("INSIGHT_TYPE", insight.getType());
    }

    @Test
    public void createInsightAssignsGUID() {
        DataServicesManager manager = DataServicesManager.getInstance();
        manager.dataCreator = new VerticalCreater();

        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertIsGUID(insight.getGUId());
    }

    private void assertIsGUID(String guid) {
        boolean isValidGUID = guid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        assertTrue("Not a valid GUID", isValidGUID);
    }

}
