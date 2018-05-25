package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.logging.model.AILCloudLogMetaData;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppInfraLoggingTest extends TestCase {

    private AppInfraLogging appInfraLogging;

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
    AILCloudLogMetaData ailCloudLogMetaData;
    @Mock
    private LoggingConfiguration loggingConfigurationMock;
    private Object[] params;
    private String componentId;
    private String componentVersion;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        params = new Object[4];
        ailCloudLogMetaData=new AILCloudLogMetaData();
        when(appInfraMock.getAppInfraContext()).thenReturn(contextMock);
        when(appInfraMock.getAppInfraContext().getApplicationInfo()).thenReturn(applicationInfoMock);
        when(loggingConfigurationMock.getAppInfra()).thenReturn(appInfraMock);
        when(appConfigurationInterfaceMock.getPropertyForKey(anyString(), anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(ConfigValues.getMockResponse());
        when(appInfraMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        AppIdentityInterface appIdentityInterface = mock(AppIdentityInterface.class);
        when(appIdentityInterface.getAppName()).thenReturn("uGrow");
        when(appIdentityInterface.getAppVersion()).thenReturn("1.0.0");
        when(appIdentityInterface.getAppState()).thenReturn(AppIdentityInterface.AppState.ACCEPTANCE);
        when(appInfraMock.getAppIdentity()).thenReturn(appIdentityInterface);
        HashMap hashMap = new HashMap();
        hashMap.put("logging.debugConfig", true);
        when(loggingConfigurationMock.getLoggingProperties()).thenReturn(hashMap);
        appInfraLogging = new AppInfraLogging(appInfraMock, componentId, componentVersion) {
            @Override
            protected Logger getJavaLogger(String componentId, String componentVersion) {
                return loggerMock;
            }

            @Override
            public AILCloudLogMetaData getAilCloudLogMetaData() {
                return ailCloudLogMetaData;
            }

            @NonNull
            @Override
            Object[] getParamObjects() {
                return params;
            }
        };

    }

    public void testLog(){
        appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.ERROR, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.INFO, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.VERBOSE, "some_event", "event_message");
        appInfraLogging.log(LoggingInterface.LogLevel.WARNING, "some_event", "event_message");
        verify(loggerMock).log(Level.WARNING, "some_event", params);
        assertEquals(params[0], "event_message");
        assertNull(params[3]);
    }

    public void testNullCheck() {
        assertNotNull(appInfraLogging.getAilCloudLogMetaData());
        assertNotNull(appInfraLogging.getParamObjects());
        assertNotNull(appInfraLogging.getJavaLogger("componentId","componentVersion"));
    }

}