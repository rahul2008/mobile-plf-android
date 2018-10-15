package com.philips.platform.core.trackers;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.verticals.VerticalCreater;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataServicesManagerInsightsTest {

    private DataServicesManager manager;

    @Before
    public void setUp() throws Exception {
        manager = DataServicesManager.getInstance();
        manager.dataCreator = new VerticalCreater();
    }

    @Test
    public void createInsightWithType() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertEquals("INSIGHT_TYPE", insight.getType());
    }

    @Test
    public void createInsightAssignsGUID() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertIsGUID(insight.getGUId());
    }

    private void assertIsGUID(String guid) {
        boolean isValidGUID = guid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        assertTrue("Not a valid GUID", isValidGUID);
    }

}
