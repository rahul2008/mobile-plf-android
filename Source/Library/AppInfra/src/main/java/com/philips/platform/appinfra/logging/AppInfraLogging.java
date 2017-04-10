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
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AppInfraLogging implements LoggingInterface {
    private static final String DIRECTORY_FILE_NAME = "AppInfraLogs";
    private static final String PROPERTIES_FILE_NAME = "logging.properties";
    private static final String APP_INFRA_LOG_FILE_NAME = "AppInfra.log%u"; //AppInfra.log0, AppInfra.log1, AppInfra.log2, AppInfra.log3, AppInfra.log4


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
            final String logLevel = (String) loggingProperty.get("logLevel");
            Boolean isConsoleLogEnabled = (Boolean) loggingProperty.get("consoleLogEnabled");
            Boolean isFileLogEnabled = (Boolean) loggingProperty.get("fileLogEnabled");
            if (!logLevel.equalsIgnoreCase("Off") && (isConsoleLogEnabled == true || isFileLogEnabled == true)) {
                javaLogger = Logger.getLogger(pComponentId); // returns new or existing log
               final Boolean isComponentLevelLogEnabled = (Boolean) loggingProperty.get("componentLevelLogEnabled");
                if (isComponentLevelLogEnabled) { // if component level filter enabled
                    // Filtering of logging components
                  final  ArrayList<String> ComponentToBeLoggedlist = new ArrayList<String>();
                    JSONArray jsonArray = (JSONArray) loggingProperty.get("componentIds");
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
            case "Error":
                javaLevel = Level.SEVERE;
                break;
            case "Warn":
                javaLevel = Level.WARNING;
                break;
            case "Info":
                javaLevel = Level.INFO;
                break;
            case "Debug":
                javaLevel = Level.CONFIG;
                break;
            case "Verbose":
                javaLevel = Level.FINE;
                break;
            case "All":
                javaLevel = Level.FINE;
                break;
            case "Off":
                javaLevel = Level.OFF;
                break;
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
                LogManager.getLogManager().readConfiguration();
                //LogManager.getLogManager().readConfiguration(mInputStream);// reads default logging.properties from AppInfra library asset in first run
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                Log.d("Logging Error", "logging.properties file missing under App assets folder", e);
            } else {
                Log.d("Logging Error", "", e);
            }
        }
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
           /* String logFileName = LogManager.getLogManager().getProperty("java.util.logging.FileHandler.pattern").trim();
            String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode is for development environment where logs and property file will be written to device external memory if available
                Log.e("App Infra log File Path", filePath);// this path will be dynamic for each device
            }
            int logFileSize = Integer.parseInt(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.limit").trim());
            int maxLogFileCount = Integer.parseInt(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.count").trim());
            boolean logFileAppendMode = Boolean.parseBoolean(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.append").trim());*/

            //////////////////////////
           final HashMap<String, Object> loggingProperty = getLoggingProperties();
            if (null == loggingProperty) {
                Log.e("AppInfra Log", "Appinfra log file name key 'logging.releaseConfig' not present in app configuration");//
                return null;
            }
            String logFileName = (String) loggingProperty.get("fileName");
            if (null == logFileName) {
                Log.e("AppInfra Log", "Appinfra log file  key 'fileName'  not present in app configuration");//
                return null;
            }
            Boolean enab = (Boolean) loggingProperty.get("fileLogEnabled");
            Integer logFileSize = (Integer) loggingProperty.get("fileSizeInBytes");
            if (logFileSize == null) {
                Log.e("AppInfra Log", "Appinfra log file  key   'fileSizeInBytes' not present in app configuration");//
                return null;
            }
            Integer maxLogFileCount = (Integer) loggingProperty.get("numberOfFiles");
            if (maxLogFileCount == null) {
                Log.e("AppInfra Log", "Appinfra log file  key 'numberOfFiles' not present in app configuration");//
                return null;
            }
            String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
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
