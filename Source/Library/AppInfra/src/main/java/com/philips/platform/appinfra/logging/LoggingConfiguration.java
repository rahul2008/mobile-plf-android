/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.appinfra.logging;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.logging.LogManager.getLogManager;

class LoggingConfiguration {

    private static final String COMPONENT_IDS_KEY = "componentIds";
    private static final String PROPERTIES_FILE_NAME = "logging.properties";
    private static final String DIRECTORY_FILE_NAME = "AppInfraLogs";
    final String LOG_LEVEL_KEY = "logLevel";
    final String CONSOLE_LOG_ENABLED_KEY = "consoleLogEnabled";
    final String FILE_LOG_ENABLED_KEY = "fileLogEnabled";
    final String COMPONENT_LEVEL_LOG_ENABLED_KEY = "componentLevelLogEnabled";
    private HashMap<String, Object> mLoggingProperties;
    private FileHandler mFileHandler;
    private AppInfra mAppInfra;
    private Logger mJavaLogger;
    private String mComponentID;
    private String mComponentVersion;

    LoggingConfiguration(AppInfra mAppInfra) {
        this.mAppInfra = mAppInfra;
    }

    boolean isComponentLevelLogEnabled(final HashMap<String, Object> loggingProperty) {
        return (null != loggingProperty.get(COMPONENT_LEVEL_LOG_ENABLED_KEY)) ? (Boolean) loggingProperty.get(COMPONENT_LEVEL_LOG_ENABLED_KEY) : false;
    }

     boolean isFileLogEnabled(final HashMap<String, Object> loggingProperty) {
        return (null != loggingProperty.get(FILE_LOG_ENABLED_KEY)) ? (Boolean) loggingProperty.get(FILE_LOG_ENABLED_KEY) : false;
    }

     boolean isConsoleLogEnabled(final HashMap<String, Object> loggingProperty) {
        return (null != loggingProperty.get(CONSOLE_LOG_ENABLED_KEY)) ? (Boolean) loggingProperty.get(CONSOLE_LOG_ENABLED_KEY) : true;
    }

     String getLogLevel(final HashMap<String, Object> loggingProperty) {
        return (null != loggingProperty.get(LOG_LEVEL_KEY)) ? (String) loggingProperty.get(LOG_LEVEL_KEY) : "All";
    }

    void  configureComponentLevelLogging(final String pComponentId, final HashMap<String, Object> loggingProperty,final String logLevel,final Boolean isConsoleLogEnabled, final Boolean isFileLogEnabled) {
        // Filtering of logging components
        final ArrayList<String> ComponentToBeLoggedList = new ArrayList<>();
        final JSONArray jsonArray = (JSONArray) loggingProperty.get(COMPONENT_IDS_KEY);
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                ComponentToBeLoggedList.add(jsonArray.optString(i));
            }
        }
        if (ComponentToBeLoggedList.contains(pComponentId)) {
            // if given component listed under config's key 'logging.debugConfig'>'componentIds' then enable log
            getJavaLogger().setLevel(getJavaLoggerLogLevel(logLevel));
            activateLogger();
            enableConsoleAndFileLog(isConsoleLogEnabled, isFileLogEnabled, mComponentID, mComponentVersion);
            getJavaLogger().log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created"); //R-AI-LOG-6
        }
    }

    HashMap<String, Object> getLoggingProperties(final AppInfra mAppInfra) {
        if (null == mLoggingProperties) {
            final String AppInfraLoggingPropertyKey;
            final boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode
                AppInfraLoggingPropertyKey = "logging.debugConfig";
            } else {
                AppInfraLoggingPropertyKey = "logging.releaseConfig";
            }
            final AppConfigurationInterface appConfigurationInterface = mAppInfra.getConfigInterface();
            final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
            mLoggingProperties = (HashMap<String, Object>) appConfigurationInterface.getPropertyForKey(AppInfraLoggingPropertyKey, "appinfra", configError);
        }
        return mLoggingProperties;
    }

    void activateLogger() {
        getJavaLogManager().addLogger(getJavaLogger());
    }

    LogManager getJavaLogManager() {
        return getLogManager();
    }


    Level getJavaLoggerLogLevel(String level) {
        final Level javaLevel;
        if (!TextUtils.isEmpty(level)) {
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
        return Level.FINE;
    }


    void enableConsoleAndFileLog(boolean consoleLog, boolean fileLog, String mComponentID, String mComponentVersion) {
        this.mComponentID = mComponentID;
        this.mComponentVersion = mComponentVersion;
        enableConsoleLog(consoleLog);
        enableFileLog(fileLog, mComponentID, mComponentVersion);
    }

    Logger getLogger(String pComponentId) {
        mJavaLogger = Logger.getLogger(pComponentId);
        return mJavaLogger;
    }

    private void enableConsoleLog(final boolean isEnabled) {

        if (isEnabled) {
            ConsoleHandler consoleHandler = getCurrentLogConsoleHandler(getJavaLogger());
            if (null == consoleHandler) {
                consoleHandler = getConsoleHandler();
                if (null != getJavaLogger() && null != getJavaLogger().getLevel()) {
                    consoleHandler.setLevel(getJavaLogger().getLevel());
                } else {
                    // for appinfra internal log mJavaLogger will be null
                    consoleHandler.setLevel(Level.FINE);
                }
                consoleHandler.setFormatter(getLogFormatter());
                // mConsoleHandler.setFilter(new LogFilter(null,"ev1"));
                getJavaLogger().addHandler(consoleHandler);
            } else {
                // nothing to do, mConsoleHandler already added to Logger
                Log.v(AppInfraLogEventID.AI_LOGGING,"Console logger already added to current Logger"+ getJavaLogger().getName());
            }

        } else { // remove console log if any
            final Handler[] currentComponentHandlers = getJavaLogger().getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        handler.close(); // flush and close connection of file
                        getJavaLogger().removeHandler(handler);
                    }
                }
            }
        }
    }

    @NonNull
    LogFormatter getLogFormatter() {
        return new LogFormatter(mComponentID, mComponentVersion, mAppInfra);
    }

    @NonNull
    ConsoleHandler getConsoleHandler() {
        return new ConsoleHandler();
    }

    private void enableFileLog(final boolean pFileLogEnabled, final String mComponentID, final String mComponentVersion) {
        if (pFileLogEnabled) {
            final FileHandler fileHandler = getCurrentLogFileHandler(getJavaLogger());
            if (null == fileHandler) {// add file log
                mFileHandler = getFileHandler();
                if (null != mJavaLogger && null != mJavaLogger.getLevel() && mFileHandler != null) {
                    mFileHandler.setLevel(getJavaLogger().getLevel());
                } else if (mFileHandler != null) {
                    // for appinfra internal log mJavaLogger will be null
                    mFileHandler.setLevel(Level.FINE);
                    mFileHandler.setFormatter(new LogFormatter(mComponentID, mComponentVersion, mAppInfra));
                    getJavaLogger().addHandler(mFileHandler);
                }


            } else {
                // nothing to do, fileHandler already added to Logger
                Log.v(AppInfraLogEventID.AI_LOGGING,"File logger already added to current Logger"+ mJavaLogger.getName());
            }
        } else { // remove file log if any
            final Handler[] currentComponentHandlers = mJavaLogger.getHandlers();
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


    // return file handler for writing logs on file based on logging.properties config
    FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        try {
            File directoryCreated = createInternalDirectory();
            final String LOG_FILE_NAME_KEY = "fileName"; //AppInfraLog0, AppInfraLog1, AppInfraLog2, AppInfraLog3, AppInfraLog4
            final HashMap<String, Object> loggingProperty = getLoggingProperties(mAppInfra);
            if (null == loggingProperty) {
                Log.e(AppInfraLogEventID.AI_LOGGING, "Appinfra log config 'logging.releaseConfig' OR 'logging.debugConfig' not present in appconfig.json so reading logging.properties file");//
                return getFileHandlerFromLoggingProperties();
            }
            final String logFileName = (String) loggingProperty.get(LOG_FILE_NAME_KEY);
            if (null == logFileName) {
                Log.e(AppInfraLogEventID.AI_LOGGING, "Appinfra log file  key 'fileName'  not present in app configuration");//
                return null;
            }
            final String LOG_FILE_SIZE_KEY = "fileSizeInBytes";
            Integer logFileSize = (Integer) loggingProperty.get(LOG_FILE_SIZE_KEY);
            if (logFileSize == null) {
                Log.e(AppInfraLogEventID.AI_LOGGING, "Appinfra log file  key   'fileSizeInBytes' not present in app configuration");//
                return null;
            }
            final String LOG_FILE_COUNT_KEY = "numberOfFiles";
            final Integer maxLogFileCount = (Integer) loggingProperty.get(LOG_FILE_COUNT_KEY);
            if (maxLogFileCount == null) {
                Log.e(AppInfraLogEventID.AI_LOGGING, "Appinfra log file  key 'numberOfFiles' not present in app configuration");//
                return null;
            }
            final String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            Log.e(AppInfraLogEventID.AI_LOGGING,"log File Path"+filePath);// this path will be dynamic for each device
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, true);
        } catch (Exception e) {
            Log.e(AppInfraLogEventID.AI_LOGGING, "FileHandler exception", e);
        }
        return fileHandler;
    }

    private FileHandler getFileHandlerFromLoggingProperties() {
        FileHandler fileHandler = null;
        try {
            final File directoryCreated = createInternalDirectory();
            final String logFileName = getLogManager().getProperty("java.util.logging.FileHandler.pattern").trim();
            final String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            final boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode is for development environment where logs and property file will be written to device external memory if available
                Log.e(AppInfraLogEventID.AI_LOGGING," log File Path"+filePath);// this path will be dynamic for each device
            }
            final int logFileSize = Integer.parseInt(getLogManager().getProperty("java.util.logging.FileHandler.limit").trim());
            final int maxLogFileCount = Integer.parseInt(getLogManager().getProperty("java.util.logging.FileHandler.count").trim());
            //boolean logFileAppendMode = Boolean.parseBoolean(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.append").trim());
            Log.e(AppInfraLogEventID.AI_LOGGING,"log File Path"+filePath);// this path will be dynamic for each device
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, true);
        } catch (Exception e) {
            Log.e(AppInfraLogEventID.AI_LOGGING, "FileHandler exception", e);
        }

        return fileHandler;
    }

    private void readLogConfigFileFromAppAsset() {
        try {
            final InputStream mInputStream = getLoggerPropertiesInputStream();
            if (mInputStream != null) {
                getLogManager().readConfiguration(mInputStream);// reads default logging.properties from AppInfra library asset in first run
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                Log.d("Logging Error", AppInfraLogEventID.AI_LOGGING + "logging.properties file missing under App assets folder", e);
            } else {
                Log.d("Logging Error", AppInfraLogEventID.AI_LOGGING, e);
            }
        }
    }

    private InputStream getLoggerPropertiesInputStream() throws IOException {
        return mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);
    }

    void fallBackToLoggingPropertiesFile(String mComponentID, String mComponentVersion) {
        this.mComponentID = mComponentID;
        this.mComponentVersion = mComponentVersion;
        readLogConfigFileFromAppAsset();
        activateLogger();
        enableConsoleLog(true);
    }


    FileHandler getCurrentLogFileHandler(Logger logger) {
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

    ConsoleHandler getCurrentLogConsoleHandler(Logger logger) {
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


    // creates or return "AppInfra Logs" at phone internal memory
    private File createInternalDirectory() {
        return mAppInfra.getAppInfraContext().getDir(DIRECTORY_FILE_NAME, Context.MODE_PRIVATE);
    }

    Logger getJavaLogger() {
        return mJavaLogger;
    }


}
