package com.philips.platform.appinfra.tagging;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 8/2/17.
 */
public class ApplicationLifeCycleHandlerTest extends AppInfraInstrumentation {

    private ApplicationLifeCycleHandler applicationLifeCycleHandler;
    private AppInfra appInfraMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        appInfraMock = mock(AppInfra.class);
        applicationLifeCycleHandler = new ApplicationLifeCycleHandler(appInfraMock);
    }

    public void testOnActivityCreated() {
        LoggingInterface loggingInterfaceMock = mock(LoggingInterface.class);
        when(appInfraMock.getAppInfraLogInstance()).thenReturn(loggingInterfaceMock);
        applicationLifeCycleHandler.onActivityCreated(null, null);
        verify(loggingInterfaceMock).log(LoggingInterface.LogLevel.VERBOSE,
                AppInfraLogEventID.AI_TAGGING, "ApplicationLifeCycleHandler Created");
    }


}