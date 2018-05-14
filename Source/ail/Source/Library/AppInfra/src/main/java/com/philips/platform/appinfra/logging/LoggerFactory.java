package com.philips.platform.appinfra.logging;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by abhishek on 4/24/18.
 */

public class LoggerFactory {
    private static Logger logger;

    public static synchronized Logger getLoggerInstance(AppInfra appInfra, LoggingConfiguration loggingConfiguration){
        if(logger==null){
            logger=createLoggerWithLogConfiguration(appInfra,loggingConfiguration);
        }
        return logger;
    }

    private static Logger createLoggerWithLogConfiguration(AppInfra appInfra, LoggingConfiguration loggingConfiguration) {
        Logger javaLogger = Logger.getLogger(appInfra.getAppInfraContext().getPackageName());
        if (loggingConfiguration.isLoggingEnabled()) {
            LogManager.getLogManager().addLogger(javaLogger);
            javaLogger.setLevel(LoggingUtils.getJavaLoggerLogLevel(loggingConfiguration.getLogLevel()));
            LogFormatter logFormatter = new LogFormatter(appInfra);
            new ConsoleLogConfigurationHandler(appInfra).handleConsoleLogConfig(logFormatter, loggingConfiguration, javaLogger);
            new CloudLogConfigHandler(appInfra).handleCloudLogConfig(loggingConfiguration, javaLogger);
            try {
                new FileLogConfigurationHandler(appInfra).handleFileLogConfig(logFormatter, loggingConfiguration, javaLogger);
            } catch (IOException e) {
                return javaLogger;
            }
            javaLogger.log(Level.CONFIG, AppInfraLogEventID.AI_LOGGING + "Logger created");
        } else {
            javaLogger.log(Level.CONFIG, AppInfraLogEventID.AI_LOGGING + "Logger created but log level is turned off in the log");
            javaLogger.setLevel(Level.OFF);
        }
        return javaLogger;
    }
}
