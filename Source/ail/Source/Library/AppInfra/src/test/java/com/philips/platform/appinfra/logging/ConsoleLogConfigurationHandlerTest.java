package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfra;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/5/18.
 */
public class ConsoleLogConfigurationHandlerTest extends TestCase {


    private ConsoleLogConfigurationHandler consoleLogConfigurationHandler;
    @Mock
    private AppInfra appInfra;
    @Mock
    private LoggingConfiguration loggingConfiguration;
    @Mock
    private LogFormatter logFormatter;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        consoleLogConfigurationHandler = new ConsoleLogConfigurationHandler(appInfra);
    }

    public void testHandleConsoleLogConfig() {
        Logger logger = Logger.getLogger("component");
        logger.setLevel(Level.ALL);
        when(loggingConfiguration.isConsoleLogEnabled()).thenReturn(true);
        when(loggingConfiguration.getLogLevel()).thenReturn("All");
        consoleLogConfigurationHandler.handleConsoleLogConfig(logFormatter, loggingConfiguration, logger);
        assertTrue(logger.getHandlers().length == 1);
        Handler handler = logger.getHandlers()[0];
        assertTrue(handler instanceof ConsoleHandler);
        assertEquals(logFormatter, handler.getFormatter());
        assertTrue(handler.getLevel() == Level.ALL);
    }

    public void testNotToAddHandlerWhenDisabled() {
        Logger logger = Logger.getLogger("component");
        logger.setLevel(Level.OFF);
        when(loggingConfiguration.isConsoleLogEnabled()).thenReturn(false);
        when(loggingConfiguration.getLogLevel()).thenReturn("All");
        consoleLogConfigurationHandler.handleConsoleLogConfig(logFormatter, loggingConfiguration, logger);
        assertTrue(logger.getHandlers().length == 0);
    }

    public void testValidatingCurrentLogConsoleHandler() {
        Logger logger = mock(Logger.class);
        logger.setLevel(Level.ALL);
        when(loggingConfiguration.isConsoleLogEnabled()).thenReturn(true);
        Handler[] handlers = new Handler[1];
        handlers[0] = new ConsoleHandler();
        when(logger.getHandlers()).thenReturn(handlers);
        consoleLogConfigurationHandler.handleConsoleLogConfig(logFormatter, loggingConfiguration, logger);

    }


}