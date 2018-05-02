package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class AppInfraLoggingTest extends TestCase {

    private AppInfraLogging appInfraLogging;
    private LoggingInterface loggingInterface;

    @Mock
    private Context contextMock;
    @Mock
    private AppInfra appInfraMock;

    @Mock
    private ApplicationInfo applicationInfoMock;

    @Mock
    private Logger loggerMock;

    @Mock
    private AppConfigurationInterface appConfigurationInterfaceMock;
    @Mock
    private LoggingConfiguration loggingConfigurationMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        String componentId = "compTest";
        String componentVersion = "2.1";
        loggingInterface = new AppInfraLogging(appInfraMock);
        Mockito.when(appInfraMock.getAppInfraContext()).thenReturn(contextMock);
        Mockito.when(appInfraMock.getAppInfraContext().getApplicationInfo()).thenReturn(applicationInfoMock);
        Mockito.when(appConfigurationInterfaceMock.getPropertyForKey(anyString(),anyString(),any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(ConfigValues.getMockResponse());
        appInfraLogging = new AppInfraLogging(appInfraMock, componentId, componentVersion) {
            @Override
            protected Logger getJavaLogger() {
                return loggerMock;
            }
        };

    }

    public void testLog(){
        appInfraLogging.createInstanceForComponent("component_id","2.1");
        appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.ERROR, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.INFO, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.VERBOSE, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.WARNING, "some_event", "event_message");

    }

}