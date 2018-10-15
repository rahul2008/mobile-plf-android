package com.philips.platform.core.trackers;


import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.verticals.VerticalCreater;
import com.philips.spy.AppConfigurationInterfaceMock;
import com.philips.spy.AppInfraInterfaceMock;
import com.philips.spy.ContextMock;
import com.philips.spy.LoggingInterfaceMock;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DataServicesManagerInsightsTest {
    @Test
    public void createInsight() {
        DataServicesManager manager = DataServicesManager.getInstance();
        final AppInfraInterfaceMock appInfraInterface = new AppInfraInterfaceMock();
        appInfraInterface.configInterface = new AppConfigurationInterfaceMock();
        appInfraInterface.logging = new LoggingInterfaceMock();
        manager.initializeDataServices(new ContextMock(), new VerticalCreater(), null, null, appInfraInterface);


        Insight insight = manager.createInsight("INSIGHT_TYPE");
        assertEquals("INSIGHT_TYPE", insight.getType());
    }
}
