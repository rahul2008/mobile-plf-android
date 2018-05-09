package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggingConfigurationTest extends AppInfraInstrumentation {

    private LoggingConfiguration loggingConfiguration;
    private AppInfra mAppInfra;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        Context context = getInstrumentation().getContext();
        mAppInfra = mock(AppInfra.class);
        when(mAppInfra.getAppInfraContext()).thenReturn(context);
        final HashMap hashMap = new HashMap();
        hashMap.put("logging.debugConfig", true);
        hashMap.put("componentLevelLogEnabled", true);
        hashMap.put("consoleLogEnabled", true);
        hashMap.put("fileLogEnabled", true);
        hashMap.put("logLevel", "All");
        loggingConfiguration = new LoggingConfiguration(mAppInfra,"","") {
            @Override
            HashMap<?, ?> getLoggingProperties() {
                return hashMap;
            }
        };
    }

    public void testIsComponentLevelLogEnabled() {
        assertTrue(loggingConfiguration.isComponentLevelLogEnabled());
    }

    public void testIsFileLogEnabled() {
        assertTrue(loggingConfiguration.isFileLogEnabled());
    }

    public void testIsConsoleLogEnabled() {
        assertTrue(loggingConfiguration.isConsoleLogEnabled());
    }

    public void testGetLogLevel() {
        assertEquals(loggingConfiguration.getLogLevel(),"All");
    }

}