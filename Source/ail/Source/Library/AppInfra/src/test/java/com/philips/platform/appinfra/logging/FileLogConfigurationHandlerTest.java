package com.philips.platform.appinfra.logging;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

import junit.framework.TestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Yogesh on 5/5/18.
 */
public class FileLogConfigurationHandlerTest extends TestCase {


    private FileLogConfigurationHandler fileLogConfigurationHandler;
    @Mock
    private AppInfra appInfra;
    @Mock
    private LoggingConfiguration loggingConfiguration;
    @Mock
    private LogFormatter logFormatter;
    @Mock
    private File file;
    @Mock
    private Context context;
    @Mock
    private FileHandler fileHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(file.getAbsolutePath()).thenReturn("/path/");
        when(appInfra.getAppInfraContext()).thenReturn(context);
        when(appInfra.getAppInfraContext().getDir("AppInfraLogs", Context.MODE_PRIVATE)).thenReturn(file);
        LoggingInterface loggingInterface = mock(LoggingInterface.class);
        when(appInfra.getAppInfraLogInstance()).thenReturn(loggingInterface);
        fileLogConfigurationHandler = new FileLogConfigurationHandler(appInfra) {
            @Override
            FileHandler getFileHandler(LoggingConfiguration loggingConfiguration) {
                return fileHandler;
            }
        };
    }

    public void testHandleConsoleLogConfig() throws IOException {
        Logger logger = Logger.getLogger("component");
        logger.setLevel(Level.ALL);
        when(loggingConfiguration.isFileLogEnabled()).thenReturn(true);
        when(loggingConfiguration.getLogLevel()).thenReturn("All");

        HashMap loggingProperty = new HashMap();
        loggingProperty.put("fileName", "some_name");
        loggingProperty.put("fileSizeInBytes", 100);
        loggingProperty.put("numberOfFiles", 100);
        when(fileHandler.getFormatter()).thenReturn(logFormatter);
        when(fileHandler.getLevel()).thenReturn(Level.ALL);
        when(loggingConfiguration.getLoggingProperties()).thenReturn(loggingProperty);
        fileLogConfigurationHandler.handleFileLogConfig(logFormatter, loggingConfiguration, logger);
        assertTrue(logger.getHandlers().length == 1);
        Handler handler = logger.getHandlers()[0];
        assertTrue(handler instanceof FileHandler);
        assertEquals(logFormatter, handler.getFormatter());
        assertTrue(handler.getLevel() == Level.ALL);
    }

    public void testNotToAddHandlerWhenDisabled() throws IOException {
        Logger logger = Logger.getLogger("component");
        logger.setLevel(Level.OFF);
        when(loggingConfiguration.isFileLogEnabled()).thenReturn(false);
        when(loggingConfiguration.getLogLevel()).thenReturn("All");
        fileLogConfigurationHandler.handleFileLogConfig(logFormatter, loggingConfiguration, logger);
        assertTrue(logger.getHandlers().length == 0);
    }

}