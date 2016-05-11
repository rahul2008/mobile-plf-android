/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.appinfra;


import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by 310238114 on 4/26/2016.
 */
public class LoggingWrapper extends AILogging {
    private static final String DIRECTORY_FILE_NAME = "AppInfra Logs";
    private static final String PROPERTIES_FILE_NAME = "logging.properties";
    private static final String APP_INFRA_LOG_FILE_NAME = "AppInfra.log";
    int AIlogFileSize= 2* 1024*1024; //2MB
    int AImaxLogFileCount = 5; // File iteration count
    boolean AIlogFileAppendMode=true; // File append


    private final String mComponentID;
    private final String mComponentVersion;
    ConsoleHandler consoleHandler;
    FileHandler fileHandler;

    Properties prop = new Properties();
    InputStream propertiesStream = null;

    Logger javaLogger;

    public LoggingWrapper(AppInfra appInfra, String componentId, String componentVersion) {
        super(appInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;
        //readLogConfigFileandAndCreateLogger(componentId);
        //init();
        readLogConfigFileandAndCreateLogger(mComponentID);

    }

    private void readLogConfigFileandAndCreateLogger(String pComponentId){
        try {
            propertiesStream = mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);
            LogManager.getLogManager().readConfiguration(propertiesStream);

           /* propertiesStream = mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);
            prop.load(propertiesStream);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        javaLogger = Logger.getLogger(pComponentId); // returns new or existing log
        LogManager.getLogManager().addLogger(javaLogger);
        // create console Handler


        enableConsoleLog(true); // enable console log by default
        javaLogger.log(Level.INFO, "Logger created"); //R-AI-LOG-6

    }




    @Override
    public void log(LogLevel level, String eventId, String message) {
        // native Java logger mapping of LOG levels
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

    /*private void init(){ // rerad
        try {

            File fileOnExternalStorage=null;
            if (0 == Environment.getExternalStorageState().compareTo(Environment.MEDIA_MOUNTED))// if device has SD card
                fileOnExternalStorage = Environment.getExternalStorageDirectory();
            else
                fileOnExternalStorage = Environment.getDataDirectory();// if device does NOT has SD card

            File fileExternalStorage = new File(fileOnExternalStorage, DIRECTORY_FILE_NAME);
            if (!fileExternalStorage.exists()) {
                fileExternalStorage.mkdirs(); // create directory in first run

                propertiesStream = mAppInfra.getAppInfraContext().getAssets().open(PROPERTIES_FILE_NAME);  // reads default logging.properties from AppInfra library asset

                if (propertiesStream != null) {
                    prop.load(propertiesStream);
                    LogManager.getLogManager().readConfiguration(propertiesStream);
                    propertiesStream.close();
                    OutputStream outputStream = new FileOutputStream(fileExternalStorage.getAbsolutePath()+"/" + PROPERTIES_FILE_NAME); // External storage
                    prop.store(outputStream,"Default config");

                    outputStream.close();
                }
            }else{ // when logging.properties file is already present in external storage
                propertiesStream = new FileInputStream(fileExternalStorage + File.separator+PROPERTIES_FILE_NAME);
                prop.load(propertiesStream);
                LogManager.getLogManager().readConfiguration(propertiesStream);

                propertiesStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/




    /*
    * Console Output: AndroidHandler should be used in place of java ConsoleHandler
    * com.android.internal.logging.AndroidHandler is responsible for displaying the logs properly in logcat.
    * AndroidHandler is necessary if you want to see the logs in logcat as well.
    * If you will not add this handler in the logging properties, all the logs will be thrown on logcat as sys.err (warn level).
    * */
    @Override
    public void enableConsoleLog(boolean isEnabled ) {
        if(isEnabled) {
            if(null==consoleHandler) {
                consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new AIlogFormatter(mComponentID, mComponentVersion));
               // consoleHandler.setFilter(new AILogFilter(null,"ev1"));
                javaLogger.addHandler(consoleHandler);
            }else{
                // nothing to do, consoleHandler already added to Logger
            }
        }else{ // remove console log if any
            Handler[] currentComponentHandlers = javaLogger.getHandlers();
            if (null!=currentComponentHandlers && currentComponentHandlers.length>0 ) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        handler.close(); // flush and close connection of file
                        javaLogger.removeHandler(handler);
                        consoleHandler=null;
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
            fileHandler.setFormatter(new AIlogFormatter(mComponentID, mComponentVersion));
            javaLogger.addHandler(fileHandler);
        }else{
                // nothing to do, fileHandler already added to Logger
            }
        }else{ // remove file log if any
            Handler[] currentComponentHandlers = javaLogger.getHandlers();
            if (null!=currentComponentHandlers && currentComponentHandlers.length>0 ) {
                // file handler will be added at index1 after console handler at 0
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof FileHandler) {
                        handler.close(); // flush and close connection of file
                        javaLogger.removeHandler(handler);
                        fileHandler=null;
                    }
                }
            }
        }
    }

    FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        File appInfraFile;
        if (0 == Environment.getExternalStorageState().compareTo(Environment.MEDIA_MOUNTED))
            appInfraFile = Environment.getExternalStorageDirectory();//
        else
            appInfraFile = Environment.getDataDirectory();

        File directoryCreated = new File(appInfraFile, DIRECTORY_FILE_NAME);
        if (!directoryCreated.exists()) {
            directoryCreated.mkdirs(); // create directory in first run
        }

        String filePath = directoryCreated.getAbsolutePath()+"/" + APP_INFRA_LOG_FILE_NAME;

        try {
            Log.e("App Infra File Path", filePath);
            fileHandler = new FileHandler(filePath, AIlogFileSize, AImaxLogFileCount, AIlogFileAppendMode);
            // fh.publish(new LogRecord(Level.ALL, "myTAG" + ": " + "finallly"));
        } catch (Exception e)
        {
            Log.e("AI Log", "FileHandler exception", e);

        }finally{
            if (fileHandler != null){
                //fh.close();
            }
        }
        return fileHandler;
    }
}
