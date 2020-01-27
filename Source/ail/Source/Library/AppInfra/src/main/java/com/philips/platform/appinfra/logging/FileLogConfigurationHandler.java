package com.philips.platform.appinfra.logging;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraLogEventID;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.LogManager.getLogManager;

/**
 * Created by abhishek on 4/24/18.
 */

public class FileLogConfigurationHandler {

    private AppInfraInterface mAppInfra;

    private static final String DIRECTORY_FILE_NAME = "AppInfraLogs";

    private FileHandler mFileHandler;

    public FileLogConfigurationHandler(AppInfraInterface appInfra) {
        mAppInfra = appInfra;
    }

    protected void handleFileLogConfig(LogFormatter logFormatter, LoggingConfiguration loggingConfiguration, @NonNull Logger logger) throws IOException {
        boolean isFileLogEnabled = loggingConfiguration.isFileLogEnabled();
        if (isFileLogEnabled) {
            final FileHandler fileHandler = getCurrentLogFileHandler(logger);

            if (null == fileHandler) {// add file log
                mFileHandler = getFileHandler(loggingConfiguration);
                Level level = logger.getLevel() != null ? logger.getLevel() : Level.FINE;

                if (mFileHandler != null) {
                    mFileHandler.setLevel(level);
                    mFileHandler.setFormatter(logFormatter);
                    logger.addHandler(mFileHandler);
                }


            } else {
                // nothing to do, fileHandler already added to Logger
                ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.VERBOSE, AppInfraLogEventID.AI_LOGGING, "File logger already added to current Logger" + logger.getName());
            }
        } else { // remove file log if any
            final Handler[] currentComponentHandlers = logger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof FileHandler) {
                        handler.close(); // flush and close connection of file
                        logger.removeHandler(handler);
                        if (mFileHandler != null) {
                            mFileHandler.flush();
                            mFileHandler.close();
                            mFileHandler = null;
                        }
                    }
                }
            }
        }
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


    // return file handler for writing logs on file based on logging.properties config
    FileHandler getFileHandler(LoggingConfiguration loggingConfiguration) {
        FileHandler fileHandler = null;
        try {
            File directoryCreated = createInternalDirectory();
            final String LOG_FILE_NAME_KEY = "fileName"; //AppInfraLog0, AppInfraLog1, AppInfraLog2, AppInfraLog3, AppInfraLog4
            final HashMap<?, ?> loggingProperty = loggingConfiguration.getLoggingProperties();
            if (null == loggingProperty) {
                if (((AppInfra)mAppInfra).getAppInfraLogInstance() != null) {
                    ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, "Appinfra log config 'logging.releaseConfig' OR 'logging.debugConfig' not present in appconfig.json so reading logging.properties file");//
                }
                return getFileHandlerFromLoggingProperties();
            }
            final String logFileName = (String) loggingProperty.get(LOG_FILE_NAME_KEY);
            if (null == logFileName) {
                if (((AppInfra)mAppInfra).getAppInfraLogInstance() != null) {
                    ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, "Appinfra log file  key 'fileName'  not present in app configuration");//
                }
                return null;
            }
            final String LOG_FILE_SIZE_KEY = "fileSizeInBytes";
            Integer logFileSize = (Integer) loggingProperty.get(LOG_FILE_SIZE_KEY);
            if (logFileSize == null) {
                if (((AppInfra)mAppInfra).getAppInfraLogInstance() != null) {
                    ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, "Appinfra log file  key   'fileSizeInBytes' not present in app configuration");//
                }
                return null;
            }
            final String LOG_FILE_COUNT_KEY = "numberOfFiles";
            final Integer maxLogFileCount = (Integer) loggingProperty.get(LOG_FILE_COUNT_KEY);
            if (maxLogFileCount == null) {
                if (((AppInfra)mAppInfra).getAppInfraLogInstance() != null) {
                    ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, "Appinfra log file  key 'numberOfFiles' not present in app configuration");//
                }
                return null;
            }
            final String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            if (((AppInfra)mAppInfra).getAppInfraLogInstance() != null) {
                ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, "log File Path" + filePath);// this path will be dynamic for each device
            }
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, true);
        } catch (Exception e) {
            if (((AppInfra)mAppInfra).getAppInfraLogInstance() != null) {
                ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LOGGING, "FileHandler exception");
            }
        }
        return fileHandler;
    }

    // creates or return "AppInfra Logs" at phone internal memory
    private File createInternalDirectory() {
        return mAppInfra.getAppInfraContext().getDir(DIRECTORY_FILE_NAME, Context.MODE_PRIVATE);
    }

    private FileHandler getFileHandlerFromLoggingProperties() {
        FileHandler fileHandler = null;
        try {
            final File directoryCreated = createInternalDirectory();
            final String logFileName = getLogManager().getProperty("java.util.logging.FileHandler.pattern").trim();
            final String filePath = directoryCreated.getAbsolutePath() + File.separator + logFileName;
            final boolean isDebuggable = (0 != (mAppInfra.getAppInfraContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
            if (isDebuggable) { // debug mode is for development environment where logs and property file will be written to device external memory if available
                ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, " log File Path" + filePath);// this path will be dynamic for each device
            }
            final int logFileSize = Integer.parseInt(getLogManager().getProperty("java.util.logging.FileHandler.limit").trim());
            final int maxLogFileCount = Integer.parseInt(getLogManager().getProperty("java.util.logging.FileHandler.count").trim());
            //boolean logFileAppendMode = Boolean.parseBoolean(LogManager.getLogManager().getProperty("java.util.logging.FileHandler.append").trim());
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_LOGGING, "log File Path" + filePath);// this path will be dynamic for each device
            fileHandler = new FileHandler(filePath, logFileSize, maxLogFileCount, true);
        } catch (Exception e) {
            ((AppInfra)mAppInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, AppInfraLogEventID.AI_LOGGING, "FileHandler exception" + e.getMessage());
        }

        return fileHandler;
    }

}
