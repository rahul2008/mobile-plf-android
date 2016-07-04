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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        readLogConfigFile();
        javaLogger = Logger.getLogger(pComponentId); // returns new or existing log
        LogManager.getLogManager().addLogger(javaLogger);
        enableConsoleLog(true); // enable console log by default
        javaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6
    }

    /* read config file from asset/loging.properties and store in phone memory in first run
       second run onwards read file from phone memory so that it can be configured at runtime
       change logging.properties file in phone memory and relaunch the App
     */
    private void readLogConfigFile(){
        try {
            File appInfraLogdirectory=getInternalOrExternalDirectory();
            String logPropertyFilePath = appInfraLogdirectory.getAbsolutePath()+File.separator + PROPERTIES_FILE_NAME;
            File logPropertyFile = new File ( logPropertyFilePath );
            //logPropertyFile.delete(); // to test first run Test this method should be uncommented
            if ( logPropertyFile.exists() ){ // when logging.properties file is already present in  phone memory
                mInputStream = new FileInputStream(logPropertyFile);
                LogManager.getLogManager().readConfiguration(mInputStream);
                mInputStream.close();
            }else{
                writeLogConfigFileInPhoneMemory(appInfraLogdirectory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLogConfigFileInPhoneMemory(File pFileExternalStorage){
        try {
            mInputStream = mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);
            if (mInputStream != null) {
                LogManager.getLogManager().readConfiguration(mInputStream);// reads default logging.properties from AppInfra library asset in first run
                mInputStream = mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME); // re initializing InputStream for writing in device else InputStream will be empty as already consumed by LogManager
                mProperties.load(mInputStream);
                OutputStream outputStream = new FileOutputStream(pFileExternalStorage.getAbsolutePath() + File.separator + PROPERTIES_FILE_NAME); // phone storage
                mProperties.store(outputStream, "Default config from App asset/logging.properties");// Write logging.properties file to phone memory for second  read onward
                mInputStream.close();
                outputStream.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }



    @Override
    public void enableConsoleLog(boolean isEnabled ) {
        boolean isDebuggable =  ( 0 != ( mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if(!isDebuggable){ // if app is in release mode then disable console logs
            consoleHandler = null;
        }else {// if app is in debug mode then only enable console logs
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
    }



    @Override
    public void enableFileLog(boolean pFileLogEnabled) {
        if(pFileLogEnabled) {
            if(null==fileHandler) {// add file log
                fileHandler = getFileHandler();
                fileHandler.setFormatter(new LogFormatter(mComponentID, mComponentVersion, mAppInfra));
                javaLogger.addHandler(fileHandler);

            }else{
                // nothing to do, fileHandler already added to Logger
            }
        }else{ // remove file log if any
            Handler[] currentComponentHandlers = javaLogger.getHandlers();
            if (null!=currentComponentHandlers && currentComponentHandlers.length>0 ) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof FileHandler) {
                        handler.close(); // flush and close connection of file
                        javaLogger.removeHandler(handler);
                        fileHandler.flush();
                        fileHandler.close();
                        fileHandler=null;
                    }
                }
            }
        }
    }

    // return file handler for writting logs on file based on logging.properties config
    private FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        try {
            File directoryCreated = getInternalOrExternalDirectory();
            String logFileName= LogManager.getLogManager().getProperty("java.util.logging.FileHandler.pattern").trim();
            String filePath = directoryCreated.getAbsolutePath()+File.separator + logFileName;
            Log.e("App Infra log File Path", filePath);// this path will be dynamic for each device
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


    // creates or return "AppInfra Logs" directory from internal/external phone memory
    private File getInternalOrExternalDirectory(){
        File appInfraLogDirectory = null;
       /* boolean isDebuggable =  ( 0 != ( mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE ) );
        if (isDebuggable) { // debug mode is for development environment where logs and property file will be written to device external memory if available
          File externalStorageRootDirectory =null;
            if (0 == Environment.getExternalStorageState().compareTo(Environment.MEDIA_MOUNTED)) {// if device has SD card or external memory
                externalStorageRootDirectory = Environment.getExternalStorageDirectory();
                appInfraLogDirectory=new File(externalStorageRootDirectory,DIRECTORY_FILE_NAME);
                if (!appInfraLogDirectory.exists()) {
                    appInfraLogDirectory.mkdirs(); // create directory in first run
                }
            }else{// if device does NOT has SD card or external memory
                appInfraLogDirectory = createInternalDirectory();  //Creating an internal dir;
            }
        }else { // release mode is for production where logs and property file will be written ONLY to device internal memory
            appInfraLogDirectory = createInternalDirectory(); //Creating an internal dir;
        }*/
        appInfraLogDirectory = createInternalDirectory(); //Creating an internal dir;
        return appInfraLogDirectory;
    }

    // creates or return "AppInfra Logs" at phone internal memory
      private File createInternalDirectory(){
        return  mAppInfra.getAppInfraContext().getDir(DIRECTORY_FILE_NAME, Context.MODE_PRIVATE);
      }

}
