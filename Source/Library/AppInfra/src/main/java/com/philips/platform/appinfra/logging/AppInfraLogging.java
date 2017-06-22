/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AppInfraLogging implements LoggingInterface {

    private static final String PROPERTIES_FILE_NAME = "logging.properties";

    private static final String DIRECTORY_FILE_NAME = "AppInfraLogs";
    private static final String LOG_FILE_NAME_KEY = "fileName"; //AppInfraLog0, AppInfraLog1, AppInfraLog2, AppInfraLog3, AppInfraLog4
    private static final String LOG_FILE_SIZE_KEY = "fileSizeInBytes";
    private static final String LOG_FILE_COUNT_KEY = "numberOfFiles";
    private static final String LOG_LEVEL_KEY = "logLevel";
    private static final String CONSOLE_LOG_ENABLED_KEY = "consoleLogEnabled";
    private static final String FILE_LOG_ENABLED_KEY = "fileLogEnabled";
    private static final String COMPONENT_LEVEL_LOG_ENABLED_KEY = "componentLevelLogEnabled";
    private static final String COMPONENT_IDS_KEY = "componentIds";


    protected String mComponentID;

    protected String mComponentVersion;


    private AppInfra mAppInfra;
    private Logger mJavaLogger;
    private ConsoleHandler mConsoleHandler;
    private FileHandler mFileHandler;
    //private Properties mProperties = new Properties();
    private InputStream mInputStream = null;
    private AppConfigurationInterface appConfigurationInterface;
    HashMap<String, Object> mLoggingProperties = null;


    public AppInfraLogging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }


    @Override
    public LoggingInterface createInstanceForComponent(String componentId, String componentVersion) {
        return new LoggingWrapper(mAppInfra, componentId, componentVersion);
    }


    @Override
    public void log(LogLevel level, String eventId, String message) {
        // native Java logger mapping of LOG levels
        if (null == mJavaLogger) {
            createLogger("");
        }
        if (null != mJavaLogger) {
            switch (level) {
                case ERROR:
                    mJavaLogger.log(Level.SEVERE, eventId, message);
                    break;
                case WARNING:
                    mJavaLogger.log(Level.WARNING, eventId, message);
                    break;
                case INFO:
                    mJavaLogger.log(Level.INFO, eventId, message);
                    break;
                case DEBUG:
                    mJavaLogger.log(Level.CONFIG, eventId, message);
                    break;
                case VERBOSE:
                    mJavaLogger.log(Level.FINE, eventId, message);
                    break;
            }
        }

    }

    /**
     * Logs message on console and file .
     *
     * @param level   the level {VERBOSE, DEBUG, INFO, WARNING, ERROR}
     * @param eventId the Event name or Tag
     * @param message the message
     * @param map the dictionary
     */
    @Override
    public void log(LogLevel level, String eventId, String message, Map<String, ?> map) {
        Object[] params = new Object[2];

        // native Java logger mapping of LOG levels
        if (null == mJavaLogger) {
            createLogger("");
        }
        if (null != mJavaLogger) {
            params[0]=message;
            params[1]=map;
            switch (level) {
                case ERROR:
                    mJavaLogger.log(Level.SEVERE, eventId, params);
                    break;
                case WARNING:
                    mJavaLogger.log(Level.WARNING, eventId, params);
                    break;
                case INFO:
                    mJavaLogger.log(Level.INFO, eventId, params);
                    break;
                case DEBUG:
                    mJavaLogger.log(Level.CONFIG, eventId, params);
                    break;
                case VERBOSE:
                    mJavaLogger.log(Level.FINE, eventId, params);
                    break;
            }
        }
    }


    protected void createLogger(String pComponentId) {

            HashMap<String, Object> loggingProperty = getLoggingProperties();
            if (null != loggingProperty) {
              /*  Log.e("appinfra", "  \"logging.debugConfig\" OR \"logging.releaseConfig\" key is missing under 'appinfra' group in AppConfig.json file");
                return;*/


                final String logLevel = (null != (String) loggingProperty.get(LOG_LEVEL_KEY)) ? (String) loggingProperty.get(LOG_LEVEL_KEY) : "All";

                final Boolean isConsoleLogEnabled = (null != (Boolean) loggingProperty.get(CONSOLE_LOG_ENABLED_KEY)) ? (Boolean) loggingProperty.get(CONSOLE_LOG_ENABLED_KEY) : true;
                final Boolean isFileLogEnabled = (null != (Boolean) loggingProperty.get(FILE_LOG_ENABLED_KEY)) ? (Boolean) loggingProperty.get(FILE_LOG_ENABLED_KEY) : false;
                if (!logLevel.equalsIgnoreCase("Off") && (isConsoleLogEnabled == true || isFileLogEnabled == true)) {
                    mJavaLogger = getLogger(pComponentId); // returns new or existing log
                    final Boolean isComponentLevelLogEnabled = (null != (Boolean) loggingProperty.get(COMPONENT_LEVEL_LOG_ENABLED_KEY)) ? (Boolean) loggingProperty.get(COMPONENT_LEVEL_LOG_ENABLED_KEY) : false;
                    if (isComponentLevelLogEnabled) { // if component level filter enabled
                        // Filtering of logging components
                        final ArrayList<String> ComponentToBeLoggedlist = new ArrayList<String>();
                        JSONArray jsonArray = (JSONArray) loggingProperty.get(COMPONENT_IDS_KEY);
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ComponentToBeLoggedlist.add(jsonArray.optString(i));
                            }
                        }
                        if (null != ComponentToBeLoggedlist && ComponentToBeLoggedlist.contains(pComponentId)) {
                            // if given component listed under config's key 'logging.debugConfig'>'componentIds' then enable log
                            mJavaLogger.setLevel(getJavaLoggerLogLevel(logLevel));
                            activateLogger();
                            enableConsoleAndFileLog(isConsoleLogEnabled, isFileLogEnabled);
                            mJavaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
                        }
                    } else { // no component level filter
                        mJavaLogger.setLevel(getJavaLoggerLogLevel(logLevel));

                        activateLogger();
                        enableConsoleAndFileLog(isConsoleLogEnabled, isFileLogEnabled);
                        mJavaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
                    }
                }
            } else {
                 /*
               FALLBACK
               if  logging.debugConfig OR  logging.releaseConfig NOT present in appconfig.json
                then read from logging.properties*/
                mJavaLogger = getLogger(pComponentId); // returns new or existing log
                readLogConfigFileFromAppAsset();
                activateLogger();
                enableConsoleLog(true);
                getJavaLogger().log(Level.INFO, "Logger created"); //R-AI-LOG-6
            }
    }

    protected Logger getJavaLogger(){
        return mJavaLogger;
    }

    protected Logger getLogger(String pComponentId) {
        mJavaLogger = Logger.getLogger(pComponentId);
        return mJavaLogger;
    }

    private void enableConsoleAndFileLog(boolean consoleLog, boolean fileLog) {
        if (consoleLog) {
            enableConsoleLog(true);
        }
        if (fileLog) {
            enableFileLog(true);
        }
    }

    private void activateLogger() {

        LogManager.getLogManager().addLogger(mJavaLogger);

    }

    private Level getJavaLoggerLogLevel(String level) {
        Level javaLevel = Level.FINE;
        switch (level) {
            case "ERROR":
            case "Error":
            case "error":
                javaLevel = Level.SEVERE;
                break;
            case "WARN":
            case "Warn":
            case "warn":
                javaLevel = Level.WARNING;
                break;
            case "INFO":
            case "Info":
            case "info":
                javaLevel = Level.INFO;
                break;
            case "DEBUG":
            case "Debug":
            case "debug":
                javaLevel = Level.CONFIG;
                break;
            case "VERBOSE":
            case "Verbose":
            case "verbose":
                javaLevel = Level.FINE;
                break;
            case "ALL":
            case "All":
            case "all":
                javaLevel = Level.FINE;
                break;
            case "OFF":
            case "Off":
            case "off":
                javaLevel = Level.OFF;
                break;
            default:
                javaLevel = Level.FINE;
        }

        return javaLevel;
    }


    protected InputStream getLoggerPropertiesInputStream() throws IOException {

        return mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);
    }


    private void readLogConfigFileFromAppAsset() {
        try {
            mInputStream = getLoggerPropertiesInputStream();
            if (mInputStream != null) {

                LogManager.getLogManager().readConfiguration(mInputStream);// reads default logging.properties from AppInfra library asset in first run
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                Log.d("Logging Error", "logging.properties file missing under App assets folder", e);
            } else {
                Log.d("Logging Error", "", e);
            }
        }
    }


    private void enableConsoleLog(boolean isEnabled) {

        if (isEnabled) {
            ConsoleHandler consoleHandler = getCurrentLogConsoleHandler(mJavaLogger);
            if (null == consoleHandler) {
                mConsoleHandler = new ConsoleHandler();
                if (null != mJavaLogger && null != mJavaLogger.getLevel()) {
                    mConsoleHandler.setLevel(mJavaLogger.getLevel());
                } else {
                    // for appinfra internal log mJavaLogger will be null
                    mConsoleHandler.setLevel(Level.FINE);
                }
                mConsoleHandler.setFormatter(new LogFormatter(mComponentID, mComponentVersion, mAppInfra));
                // mConsoleHandler.setFilter(new LogFilter(null,"ev1"));
                mJavaLogger.addHandler(mConsoleHandler);
            } else {
                // nothing to do, mConsoleHandler already added to Logger
                Log.v("AILOG","Console logger already added to current Logger"+ mJavaLogger.getName());
            }

        } else { // remove console log if any
            Handler[] currentComponentHandlers = mJavaLogger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        handler.close(); // flush and close connection of file
                        mJavaLogger.removeHandler(handler);
                        mConsoleHandler = null;
                    }
                }
            }
        }
    }


    private void enableFileLog(boolean pFileLogEnabled) {
        if (pFileLogEnabled) {
                FileHandler fileHandler = getCurrentLogFileHandler(mJavaLogger);
            if (null == fileHandler) {// add file log
                mFileHandler = getFileHandler();
                if (null != mJavaLogger && null != mJavaLogger.getLevel()) {
                    mFileHandler.setLevel(mJavaLogger.getLevel());
                } else {
                    // for appinfra internal log mJavaLogger will be null
                    mFileHandler.setLevel(Level.FINE);
                }
                mFileHandler.setFormatter(new LogFormatter(mComponentID, mComponentVersion, mAppInfra));
                mJavaLogger.addHandler(mFileHandler);

            } else {
                // nothing to do, fileHandler already added to Logger
                Log.v("AILOG","File logger already added to current Logger"+ mJavaLogger.getName());
            }
        } else { // remove file log if any
            Handler[] currentComponentHandlers = mJavaLogger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof FileHandler) {
                        handler.close(); // flush and close connection of file
                        mJavaLogger.removeHandler(handler);
                        mFileHandler.flush();
                        mFileHandler.close();
                        mFileHandler = null;
                    }
                }
            }
        }
    }

    // return file handler for writting logs on file based on logging.properties config
    private FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        try {
            File directoryCreated = createInternalDirectory();

            final HashMap<String, Object> loggingProperty = getLoggingProperties();
            if (null == loggingProperty) {
                Log.e("AppInfra Log", "Appinfra log config 'logging.releaseConfig' OR 'logging.debugConfig' not present in appconfig.json so reading logging.properties file");//
                return getFileHandlerFromLoggingProperties();
            }
            final String logFileName = (String) loggingProperty.get(LOG_FILE_NAME_KEY);
            if (null == logFileName) {
                Log.e("AppInfra Log", "Appinfra log file  key 'fileName'  not present in app configuration");//
                return null;
            }

            Integer logFileSize = (Integer) loggingProperty.get(LOG_FILE_SIZE_KEY);
            if (logFileSize == null) {
                Log.e("AppInfra Log", "Appinfra log file  key   'fileSizeInBytes' not present in app configuration");//
                return null;
            }
            final Integer maxLogFileCount = (Integer) loggingProperty.get(LOG_FILE_COUNT_KEY);
            if (maxLogFileCount == null) {
                Log.e("AppInfra Log", "Appinfra log file  key 'numberOfFiles' not present in app configuration");//
                return null;
            }
            final String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            Log.e("App Infra log File Path", filePath);// this path will be dynamic for each device
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, true);
        } catch (Exception e) {
            Log.e("AI Log", "FileHandler exception", e);
        }
        return fileHandler;
    }

    private FileHandler getFileHandlerFromLoggingProperties() {
        FileHandler fileHandler = null;
        try {
            File directoryCreated = createInternalDirectory();
            String logFileName = LogManager.getLogManager().getProperty("java.util.logging.FileHandler.pattern").trim();
            String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode is for development environment where logs and property file will be written to device external memory if available
                Log.e("App Infra log File Path", filePath);// this path will be dynamic for each device
            }
            int logFileSize = Integer.parseInt(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.limit").trim());
            int maxLogFileCount = Integer.parseInt(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.count").trim());
            //boolean logFileAppendMode = Boolean.parseBoolean(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.append").trim());
            Log.e("App Infra log File Path", filePath);// this path will be dynamic for each device
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, true);
        } catch (Exception e) {
            Log.e("AI Log", "FileHandler exception", e);
        }
        return fileHandler;
    }


    // creates or return "AppInfra Logs" at phone internal memory
    private File createInternalDirectory() {
        return mAppInfra.getAppInfraContext().getDir(DIRECTORY_FILE_NAME, Context.MODE_PRIVATE);
    }

    HashMap<String, Object> getLoggingProperties() {
        if (null == mLoggingProperties) {
            String AppinfraLoggingPropertKey;
            final boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode
                AppinfraLoggingPropertKey = "logging.debugConfig";
            } else {
                AppinfraLoggingPropertKey = "logging.releaseConfig";
            }
            AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
            AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
            mLoggingProperties = (HashMap<String, Object>) appConfigurationInterface.getPropertyForKey(AppinfraLoggingPropertKey, "appinfra", configError);
        }
        return mLoggingProperties;
    }

    private FileHandler getCurrentLogFileHandler(Logger logger) {
        FileHandler logFileHandler = null;
        if (null != logger) {
            Handler[] currentComponentHandlers = logger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof FileHandler) {
                        logFileHandler = (FileHandler) handler;
                        break;
                    }
                }
            }
        }
        return logFileHandler;
    }

    private ConsoleHandler getCurrentLogConsoleHandler(Logger logger) {
        ConsoleHandler logConsoleHandler = null;
        if (null != logger) {
            Handler[] currentComponentHandlers = logger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        logConsoleHandler = (ConsoleHandler) handler;
                        break;
                    }
                }
            }
        }
        return logConsoleHandler;
    }

}
