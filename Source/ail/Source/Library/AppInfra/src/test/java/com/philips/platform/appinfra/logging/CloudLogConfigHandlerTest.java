package com.philips.platform.appinfra.logging;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/24/18.
 */
public class CloudLogConfigHandlerTest extends TestCase {

    private CloudLogConfigHandler cloudLogConfigHandler;

    @Mock
    private AppInfraInterface appInfra;

    @Mock
    private LoggingConfiguration loggingConfiguration;

    @Mock
    private Logger logger;

    @Mock
    private CloudLogHandler cloudLogHandler;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        cloudLogConfigHandler = new CloudLogConfigHandler(appInfra) {
            @NonNull
            @Override
            CloudLogHandler getCloudLogHandler() {
                return cloudLogHandler;
            }
        };
    }

    public void testValidatingCloudLogConfig() {
        when(loggingConfiguration.isCloudLogEnabled()).thenReturn(true);
        cloudLogConfigHandler.handleCloudLogConfig(loggingConfiguration, logger);
        verify(cloudLogHandler).setLevel(Level.FINE);
        when(logger.getLevel()).thenReturn(Level.ALL);
        cloudLogConfigHandler.handleCloudLogConfig(loggingConfiguration, logger);
        verify(cloudLogHandler).setLevel(logger.getLevel());
        verify(logger, atLeastOnce()).addHandler(cloudLogHandler);
        Handler[] handlers = new Handler[2];
        handlers[0] = new ConsoleHandler();
        handlers[1] = cloudLogHandler;
        when(logger.getHandlers()).thenReturn(handlers);
        when(loggingConfiguration.isCloudLogEnabled()).thenReturn(false);
        cloudLogConfigHandler.handleCloudLogConfig(loggingConfiguration, logger);
        verify(cloudLogHandler).close();
        verify(logger).removeHandler(cloudLogHandler);
    }

}