package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoggingConfigurationTest extends TestCase {

    private LoggingConfiguration loggingConfiguration;
    private AppInfra mAppInfra;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        Context context = mock(Context.class);
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

    public void testAssertingNonNull() {
        assertNotNull(loggingConfiguration.getAppInfra());
        assertNotNull(loggingConfiguration.getComponentVersion());
    }

    public void testIsLoggingEnabled() {
        boolean loggingEnabled = loggingConfiguration.isLoggingEnabled();
        assertTrue(loggingEnabled);
        final HashMap hashMap = new HashMap();
        hashMap.put("logLevel", "Off");
        loggingConfiguration = new LoggingConfiguration(mAppInfra,"","") {
            @Override
            HashMap<?, ?> getLoggingProperties() {
                return hashMap;
            }
        };
        loggingEnabled = loggingConfiguration.isLoggingEnabled();
        assertFalse(loggingEnabled);
    }

}