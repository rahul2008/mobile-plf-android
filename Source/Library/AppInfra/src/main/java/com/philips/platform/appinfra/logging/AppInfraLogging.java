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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AppInfraLogging implements LoggingInterface {
    private static final String DIRECTORY_FILE_NAME = "AppInfraLogs";

    private static final String LOG_FILE_NAME_KEY = "fileName"; //AppInfraLog0, AppInfraLog1, AppInfraLog2, AppInfraLog3, AppInfraLog4
    private static final String LOG_FILE_SIZE_KEY = "fileSizeInBytes";
    private static final String LOG_FILE_COUNT_KEY = "numberOfFiles";
    private static final String LOG_LEVEL_KEY = "logLevel";
    private static final String CONSOLE_LOG_ENABLED_KEY = "consoleLogEnabled";
    private static final String FILE_LOG_ENABLED_KEY = "fileLogEnabled";
    private static final String COMPONENT_LEVEL_LOG_ENABLED_KEY = "componentLevelLogEnabled";
    private static final String COMPONENT_IDS_KEY ="componentIds";



    protected String mComponentID;

    protected String mComponentVersion;


    private AppInfra mAppInfra;
    private Logger javaLogger;
    private ConsoleHandler consoleHandler;
    private FileHandler fileHandler;
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
        if (null == javaLogger) {
            createLogger("");
        }
        switch (level) {
            case ERROR:
                javaLogger.log(Level.SEVERE, eventId, message);
                break;
            case WARNING:
                javaLogger.log(Level.WARNING, eventId, message);
                break;
            case INFO:
                javaLogger.log(Level.INFO, eventId, message);
                break;
            case DEBUG:
                javaLogger.log(Level.CONFIG, eventId, message);
                break;
            case VERBOSE:
                javaLogger.log(Level.FINE, eventId, message);
                break;
        }

    }


    protected void createLogger(String pComponentId) {


        if (null != pComponentId && pComponentId.equalsIgnoreCase(mAppInfra.getComponentId())) {
            javaLogger = Logger.getLogger(pComponentId); // returns new or existing log
            activateLogger();
            enableConsoleLog(true);
            javaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
        }else if (null != pComponentId && !mAppInfra.getComponentId().equalsIgnoreCase(pComponentId)) { //all logger except AppInfra internal logging
            HashMap<String, Object> loggingProperty = getLoggingProperties();
            if (null == loggingProperty) {
                Log.e("appinfra", "  \"logging.debugConfig\" OR \"logging.releaseConfig\" key is missing under 'appinfra' group in AppConfig.json file");
                return;
            }
            final String logLevel = (String) loggingProperty.get(LOG_LEVEL_KEY);
            final Boolean isConsoleLogEnabled = (Boolean) loggingProperty.get(CONSOLE_LOG_ENABLED_KEY);
            final Boolean isFileLogEnabled = (Boolean) loggingProperty.get(FILE_LOG_ENABLED_KEY);
            if (!logLevel.equalsIgnoreCase("Off") && (isConsoleLogEnabled == true || isFileLogEnabled == true)) {
                javaLogger = Logger.getLogger(pComponentId); // returns new or existing log
               final Boolean isComponentLevelLogEnabled = (Boolean) loggingProperty.get(COMPONENT_LEVEL_LOG_ENABLED_KEY);
                if (isComponentLevelLogEnabled) { // if component level filter enabled
                    // Filtering of logging components
                  final  ArrayList<String> ComponentToBeLoggedlist = new ArrayList<String>();
                    JSONArray jsonArray = (JSONArray) loggingProperty.get(COMPONENT_IDS_KEY);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ComponentToBeLoggedlist.add(jsonArray.optString(i));
                        }
                    }
                    if (null != ComponentToBeLoggedlist && ComponentToBeLoggedlist.contains(pComponentId)) {
                        // if given component listed under config's key 'logging.debugConfig'>'componentIds' then enable log
                        javaLogger.setLevel(getJavaLoggerLogLevel(logLevel));
                        activateLogger();
                        enableConsoleAndFileLog(isConsoleLogEnabled,isFileLogEnabled);
                        javaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
                    }
                } else { // no component level filter
                    javaLogger.setLevel(getJavaLoggerLogLevel(logLevel));
                    activateLogger();
                    enableConsoleAndFileLog(isConsoleLogEnabled,isFileLogEnabled);
                    javaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
                }

                //enable console/file log


            }

        }

    }

    private void enableConsoleAndFileLog(boolean consoleLog, boolean fileLog){
        if (consoleLog) {
            enableConsoleLog(true);
        }
        if (fileLog) {
            enableFileLog(true);
        }
    }
    private void activateLogger() {
        LogManager.getLogManager().addLogger(javaLogger);

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
        }

        return javaLevel;
    }






    /**
     * @param isEnabled
     * @deprecated
     */
    @Deprecated
    public void enableConsoleLog(boolean isEnabled) {

        if (isEnabled) {
            if (null == consoleHandler) {
                consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new LogFormatter(mComponentID, mComponentVersion, mAppInfra));
                // consoleHandler.setFilter(new LogFilter(null,"ev1"));
                javaLogger.addHandler(consoleHandler);
            } else {
                // nothing to do, consoleHandler already added to Logger
            }
        } else { // remove console log if any
            Handler[] currentComponentHandlers = javaLogger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        handler.close(); // flush and close connection of file
                        javaLogger.removeHandler(handler);
                        consoleHandler = null;
                    }
                }
            }
        }
    }


    @Override
    @Deprecated
    public void enableFileLog(boolean pFileLogEnabled) {
        if (pFileLogEnabled) {
            if (null == fileHandler) {// add file log
                fileHandler = getFileHandler();
                fileHandler.setFormatter(new LogFormatter(mComponentID, mComponentVersion, mAppInfra));
                javaLogger.addHandler(fileHandler);

            } else {
                // nothing to do, fileHandler already added to Logger
            }
        } else { // remove file log if any
            Handler[] currentComponentHandlers = javaLogger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof FileHandler) {
                        handler.close(); // flush and close connection of file
                        javaLogger.removeHandler(handler);
                        fileHandler.flush();
                        fileHandler.close();
                        fileHandler = null;
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
                Log.e("AppInfra Log", "Appinfra log file name key 'logging.releaseConfig' not present in app configuration");//
                return null;
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

}
