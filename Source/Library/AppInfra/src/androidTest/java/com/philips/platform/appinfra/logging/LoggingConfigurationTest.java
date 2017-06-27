package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.AppInfraLogEventID;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggingConfigurationTest extends AppInfraInstrumentation {

    private LoggingConfiguration loggingConfiguration;
    private AppInfra mAppInfra;
    private Logger logger;
    private LogManager logManager;
    private String config = "{\n" +
            "  \"LOGGING.RELEASECONFIG\": {\n" +
            "    \"fileName\": \"AppInfraLog\",\n" +
            "    \"numberOfFiles\": 5,\n" +
            "    \"fileSizeInBytes\": 50000,\n" +
            "    \"logLevel\": \"All\",\n" +
            "    \"fileLogEnabled\": true,\n" +
            "    \"consoleLogEnabled\": true,\n" +
            "    \"componentLevelLogEnabled\": false,\n" +
            "    \"componentIds\": [\n" +
            "      \"DemoAppInfra\",\n" +
            "      \"Registration\"\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        logger = mock(Logger.class);
        logManager = mock(LogManager.class);
        Context context = getInstrumentation().getContext();
        mAppInfra = new AppInfra.Builder().build(context);
        mAppInfra.getLogging().createInstanceForComponent("ail","1.5");
        loggingConfiguration = new LoggingConfiguration(mAppInfra) {
            @Override
            public Logger getJavaLogger() {
                return logger;
            }

            @Override
            LogManager getJavaLogManager() {
                return logManager;
            }
        };
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

    public void testGetLogLevel() {
        HashMap<String, Object> loggingProperty = new HashMap<>();
        loggingProperty.put(loggingConfiguration.LOG_LEVEL_KEY, "Test");
        assertEquals(loggingConfiguration.getLogLevel(loggingProperty),"Test");

        loggingProperty.put(loggingConfiguration.LOG_LEVEL_KEY, null);
        assertEquals(loggingConfiguration.getLogLevel(loggingProperty),"All");
    }

    public void testGettingLogger() {
        assertNotNull(loggingConfiguration.getLogger("component_id"));
    }

    public void testGetJavaLogLevel() {
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("ERROR"), Level.SEVERE);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("WARN"), Level.WARNING);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("INFO"), Level.INFO);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("DEBUG"), Level.CONFIG);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("VERBOSE"), Level.FINE);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("ALL"), Level.FINE);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("OFF"), Level.OFF);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel("off"), Level.OFF);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel(null), Level.FINE);
        assertEquals(loggingConfiguration.getJavaLoggerLogLevel(""), Level.FINE);
    }

    public void testConfigureComponentLevelLogging() {
        try {
            JSONObject jsonObject = new JSONObject(config);
            JSONObject releaseConfig = jsonObject.getJSONObject("LOGGING.RELEASECONFIG");
            HashMap<String, Object> loggingProperty = new HashMap<>();
            loggingProperty.put("componentIds", releaseConfig.getJSONArray("componentIds"));
            String logLevel = releaseConfig.getString("logLevel");
            loggingConfiguration.configureComponentLevelLogging("DemoAppInfra", loggingProperty, logLevel, releaseConfig.getBoolean("consoleLogEnabled"), releaseConfig.getBoolean("fileLogEnabled"));
            verify(logger).setLevel(loggingConfiguration.getJavaLoggerLogLevel(logLevel));
            verify(logManager).addLogger(logger);
            verify(logger).log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void testConsoleLogConfiguration() {
        final ConsoleHandler consoleHandler = mock(ConsoleHandler.class);
        final LogFormatter logFormatter = mock(LogFormatter.class);
        try {
            loggingConfiguration = new LoggingConfiguration(mAppInfra) {
                @Override
                public Logger getJavaLogger() {
                    return logger;
                }

                @Override
                LogManager getJavaLogManager() {
                    return logManager;
                }

                @Override
                ConsoleHandler getCurrentLogConsoleHandler(Logger logger) {
                    return null;
                }

                @NonNull
                @Override
                ConsoleHandler getConsoleHandler() {
                    return consoleHandler;
                }

                @NonNull
                @Override
                LogFormatter getLogFormatter() {
                    return logFormatter;
                }
            };
            ConsoleHandler consoleHandler1 = mock(ConsoleHandler.class);
            Handler[] handlers = {consoleHandler1};
            when(logger.getHandlers()).thenReturn(handlers);
            JSONObject jsonObject = new JSONObject(config);
            JSONObject releaseConfig = jsonObject.getJSONObject("LOGGING.RELEASECONFIG");
            HashMap<String, Object> loggingProperty = new HashMap<>();
            loggingProperty.put("componentIds", releaseConfig.getJSONArray("componentIds"));
            String logLevel = releaseConfig.getString("logLevel");
            loggingConfiguration.configureComponentLevelLogging("DemoAppInfra", loggingProperty, logLevel, false, releaseConfig.getBoolean("fileLogEnabled"));
            verify(consoleHandler1).close();
            verify(logger).removeHandler(consoleHandler1);

            loggingConfiguration.configureComponentLevelLogging("DemoAppInfra", loggingProperty, logLevel, releaseConfig.getBoolean("consoleLogEnabled"), releaseConfig.getBoolean("fileLogEnabled"));
            verify(consoleHandler).setFormatter(logFormatter);
            verify(logger).addHandler(consoleHandler);
            verify(consoleHandler).setLevel(Level.FINE);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void testFileLogConfiguration() {
        final FileHandler fileHandler = mock(FileHandler.class);
        final LogFormatter logFormatter = mock(LogFormatter.class);
        try {
            loggingConfiguration = new LoggingConfiguration(mAppInfra) {
                @Override
                public Logger getJavaLogger() {
                    return logger;
                }

                @Override
                LogManager getJavaLogManager() {
                    return logManager;
                }

                @Override
                FileHandler getCurrentLogFileHandler(Logger logger) {
                    return null;
                }

                @Override
                FileHandler getFileHandler() {
                    return fileHandler;
                }

                @NonNull
                @Override
                LogFormatter getLogFormatter() {
                    return logFormatter;
                }
            };
            FileHandler fileHandler1 = mock(FileHandler.class);
            Handler[] handlers = {fileHandler1};
            when(logger.getHandlers()).thenReturn(handlers);
            JSONObject jsonObject = new JSONObject(config);
            JSONObject releaseConfig = jsonObject.getJSONObject("LOGGING.RELEASECONFIG");
            HashMap<String, Object> loggingProperty = new HashMap<>();
            loggingProperty.put("componentIds", releaseConfig.getJSONArray("componentIds"));
            String logLevel = releaseConfig.getString("logLevel");
            loggingConfiguration.configureComponentLevelLogging("DemoAppInfra", loggingProperty, logLevel, jsonObject.getBoolean("consoleLogEnabled"), false);
            verify(fileHandler1).close();
            verify(logger).removeHandler(fileHandler1);

            loggingConfiguration.configureComponentLevelLogging("DemoAppInfra", loggingProperty, logLevel, releaseConfig.getBoolean("consoleLogEnabled"), releaseConfig.getBoolean("fileLogEnabled"));
            verify(fileHandler).setFormatter(logFormatter);
            verify(logger).addHandler(fileHandler);
            verify(fileHandler).setLevel(Level.FINE);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

}