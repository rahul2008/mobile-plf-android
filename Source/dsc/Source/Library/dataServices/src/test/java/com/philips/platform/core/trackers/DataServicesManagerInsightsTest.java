package com.philips.platform.core.trackers;


import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.events.InsightsSaveRequest;
import com.philips.platform.verticals.VerticalCreater;
import com.philips.spy.EventingMock;
import com.philips.spy.InsightDBRequestListenerMock;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        assertIsGUID(insight.getSynchronisationData().getGuid());
    }

    @Test
    public void createInsightIsActive() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertFalse(insight.getSynchronisationData().isInactive());
    }

    @Test
    public void createInsightLastModifiedIsNull() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertNull(insight.getSynchronisationData().getLastModified());
    }

    @Test
    public void createInsightVersionIsZero() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertEquals(0, insight.getSynchronisationData().getVersion());
    }

    @Test
    public void saveEmptyListOfInsightsPostsEventOnBus() {
        manager.saveInsights(Collections.emptyList(), dbListener);
        assertEquals(InsightsSaveRequest.class, eventing.postedEvent.getClass());
        assertEquals(Collections.emptyList(), getPostInsightsSaveRequest().getInsights());
    }

    @Test
    public void saveListOfInsightsPostsEventOnBus() {
        final List<Insight> insightsToSave = createListOfInsights();
        manager.saveInsights(insightsToSave, dbListener);
        assertEquals(insightsToSave, getPostInsightsSaveRequest().getInsights());
    }

    @Test
    public void saveListOfInsightsPostsEventContainingDbListener() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        manager.saveInsights(Collections.singletonList(insight), dbListener);
        assertEquals(dbListener, getPostInsightsSaveRequest().getDbRequestListener());
    }

    @NonNull
    private List<Insight> createListOfInsights() {
        Insight insight = manager.createInsight("INSIGHT_TYPE");
        Insight anotherInsight = manager.createInsight("INSIGHT_TYPE");
        return Arrays.asList(insight, anotherInsight);
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
