package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

public class LoggingConfigurationTest extends AppInfraInstrumentation {

    private LoggingConfiguration loggingConfiguration;
    private Context context;
    private AppInfra mAppInfra;

    @Before
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = getInstrumentation().getContext();
        mAppInfra = new AppInfra.Builder().build(context);
        loggingConfiguration = new LoggingConfiguration(mAppInfra);
    }

    public void testIsComponentLevelLogEnabled() {
        HashMap<String, Object> loggingProperty = new HashMap<>();
        loggingProperty.put(loggingConfiguration.COMPONENT_LEVEL_LOG_ENABLED_KEY, true);
        assertTrue(loggingConfiguration.isComponentLevelLogEnabled(loggingProperty));

        loggingProperty.put(loggingConfiguration.COMPONENT_LEVEL_LOG_ENABLED_KEY, false);
        assertFalse(loggingConfiguration.isComponentLevelLogEnabled(loggingProperty));
    }

    public void testIsFileLogEnabled() {
        HashMap<String, Object> loggingProperty = new HashMap<>();
        loggingProperty.put(loggingConfiguration.FILE_LOG_ENABLED_KEY, true);
        assertTrue(loggingConfiguration.isFileLogEnabled(loggingProperty));

        loggingProperty.put(loggingConfiguration.FILE_LOG_ENABLED_KEY, false);
        assertFalse(loggingConfiguration.isFileLogEnabled(loggingProperty));
    }

    public void testIsConsoleLogEnabled() {
        HashMap<String, Object> loggingProperty = new HashMap<>();
        loggingProperty.put(loggingConfiguration.CONSOLE_LOG_ENABLED_KEY, true);
        assertTrue(loggingConfiguration.isConsoleLogEnabled(loggingProperty));

        loggingProperty.put(loggingConfiguration.CONSOLE_LOG_ENABLED_KEY, false);
        assertFalse(loggingConfiguration.isConsoleLogEnabled(loggingProperty));
    }

    public void testgetLogLevel() {
        HashMap<String, Object> loggingProperty = new HashMap<>();
        loggingProperty.put(loggingConfiguration.LOG_LEVEL_KEY, "Test");
        assertEquals(loggingConfiguration.getLogLevel(loggingProperty),"Test");

        loggingProperty.put(loggingConfiguration.LOG_LEVEL_KEY, null);
        assertEquals(loggingConfiguration.getLogLevel(loggingProperty),"All");
    }

}