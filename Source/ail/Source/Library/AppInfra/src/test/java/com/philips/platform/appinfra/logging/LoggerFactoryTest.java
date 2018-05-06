package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.BuildConfig;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.Logger;

import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/5/18.
 */
public class LoggerFactoryTest extends TestCase {


    private LoggerFactory loggerFactory;
    @Mock
    private AppInfra appInfra;
    @Mock
    private LoggingConfiguration loggingConfiguration;
    @Mock
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(appInfra.getAppInfraContext()).thenReturn(context);
        when(loggingConfiguration.isLoggingEnabled()).thenReturn(true);
        when(loggingConfiguration.getComponentID()).thenReturn("component_id");
        when(loggingConfiguration.getLogLevel()).thenReturn("VERBOSE");
        when(loggingConfiguration.getComponentVersion()).thenReturn(BuildConfig.VERSION_NAME);
        when(loggingConfiguration.getAppInfra()).thenReturn(appInfra);
    }

    public void testCreatingLogger() {
        Logger logger = LoggerFactory.createLoggerWithLogConfiguration(appInfra, loggingConfiguration);
        assertTrue(logger.getHandlers().length == 0);
        assertTrue(logger.getName().equals("component_id"));
    }
}