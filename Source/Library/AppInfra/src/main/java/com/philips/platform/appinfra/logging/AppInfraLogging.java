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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class AppInfraLogging implements  LoggingInterface {
    private static final String DIRECTORY_FILE_NAME = "AppInfra Logs";
    private static final String PROPERTIES_FILE_NAME = "logging.properties";
    private static final String APP_INFRA_LOG_FILE_NAME = "AppInfra.log%u"; //AppInfra.log0, AppInfra.log1, AppInfra.log2, AppInfra.log3, AppInfra.log4


    protected String mComponentID;

    protected String mComponentVersion;


    private AppInfra mAppInfra;
    private Logger javaLogger;
    private ConsoleHandler consoleHandler;
    private FileHandler fileHandler;
    private Properties mProperties = new Properties();
    private InputStream mInputStream = null;



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
        if(null==javaLogger){
            createLogger("");
        }
        switch(level){
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




    protected void createLogger(String pComponentId){
        readLogConfigFileFromAppAsset();
        javaLogger = Logger.getLogger(pComponentId); // returns new or existing log
        LogManager.getLogManager().addLogger(javaLogger);
        enableConsoleLog(true); // enable console log by default
        javaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
    }

    private void readLogConfigFileFromAppAsset(){
        try {
            mInputStream = mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);
            if (mInputStream != null) {
                LogManager.getLogManager().readConfiguration(mInputStream);// reads default logging.properties from AppInfra library asset in first run
            }
        }catch(IOException e){
            if(e instanceof FileNotFoundException){
                Log.d("Logging Error","logging.properties file missing under App assets folder",e);
            }else{
                Log.d("Logging Error","",e);
            }
        }
        }





    @Override
    public void enableConsoleLog(boolean isEnabled ) {
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
            String logFileName= LogManager.getLogManager().getProperty("java.util.logging.FileHandler.pattern").trim();
            String filePath = directoryCreated.getAbsolutePath()+File.separator + logFileName;
            boolean isDebuggable =  ( 0 != ( mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
            if (isDebuggable) { // debug mode is for development environment where logs and property file will be written to device external memory if available
                Log.e("App Infra log File Path", filePath);// this path will be dynamic for each device
            }
            int logFileSize = Integer.parseInt(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.limit").trim());
            int maxLogFileCount = Integer.parseInt(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.count").trim());
            boolean logFileAppendMode = Boolean.parseBoolean(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.append").trim());
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, logFileAppendMode);
        } catch (Exception e)
        {
            Log.e("AI Log", "FileHandler exception", e);
        }
        return fileHandler;
    }


    // creates or return "AppInfra Logs" at phone internal memory
      private File createInternalDirectory(){
        return  mAppInfra.getAppInfraContext().getDir(DIRECTORY_FILE_NAME, Context.MODE_PRIVATE);
      }

}
