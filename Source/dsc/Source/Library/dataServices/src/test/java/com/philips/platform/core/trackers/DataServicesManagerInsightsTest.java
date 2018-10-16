package com.philips.platform.core.trackers;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.InsightsSaveRequest;
import com.philips.platform.verticals.VerticalCreater;
import com.philips.spy.EventingMock;
import com.philips.spy.InsightDBRequestListenerMock;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataServicesManagerInsightsTest {

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

    @Test
    public void saveEmptyListOfInsightsPostsEventOnBus() {
        manager.saveInsights(Collections.emptyList(), dbListener);

        assertEquals(InsightsSaveRequest.class, eventing.postedEvent.getClass());
        assertEquals(Collections.emptyList(), getPostInsightsSaveRequest().getInsights());
    }

    @Test
    public void saveListOfInsightsPostsEventOnBus() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        manager.saveInsights(Collections.singletonList(insight), dbListener);
        assertEquals(Collections.singletonList(insight), getPostInsightsSaveRequest().getInsights());
    }

    @Test
    public void saveListOfInsightsPostsEventContainingDbListener() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        manager.saveInsights(Collections.singletonList(insight), dbListener);
        assertEquals(dbListener, getPostInsightsSaveRequest().getDbListener());
    }

    private InsightsSaveRequest getPostInsightsSaveRequest() {
        return (InsightsSaveRequest) eventing.postedEvent;
    }

    private void assertIsGUID(String guid) {
        boolean isValidGUID = guid.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        assertTrue("Not a valid GUID", isValidGUID);
    }

    @Before
    public void setUp() throws Exception {
        manager = DataServicesManager.getInstance();
        manager.dataCreator = new VerticalCreater();
        eventing = new EventingMock();
        manager.mEventing = eventing;
        dbListener = new InsightDBRequestListenerMock();
    }

    private DataServicesManager manager;
    private EventingMock eventing;
    private InsightDBRequestListenerMock dbListener;
}
