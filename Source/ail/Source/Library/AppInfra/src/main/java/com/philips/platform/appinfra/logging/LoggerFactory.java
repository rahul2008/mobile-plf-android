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

class LoggerFactory {
    protected static Logger createLoggerWithLogConfiguration(AppInfra appInfra,LoggingConfiguration loggingConfiguration) {
        Logger javaLogger = Logger.getLogger(loggingConfiguration.getComponentID());
        if (loggingConfiguration.isLoggingEnabled()) {
            LogManager.getLogManager().addLogger(javaLogger);
            javaLogger.setLevel(LoggingUtils.getJavaLoggerLogLevel(loggingConfiguration.getLogLevel()));
            LogFormatter logFormatter = new LogFormatter(loggingConfiguration.getComponentID(), loggingConfiguration.getComponentVersion(), loggingConfiguration.getAppInfra());
            new ConsoleLogConfigurationHandler().handleConsoleLogConfig(logFormatter, loggingConfiguration, javaLogger);
            new CloudLogConfigHandler().handleCloudLogConfig(logFormatter, loggingConfiguration, javaLogger);
            try {
                new FileLogConfigurationHandler(appInfra).handleFileLogConfig(logFormatter, loggingConfiguration, javaLogger);
            } catch (IOException e) {
                return javaLogger;
            }
            javaLogger.log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created");
        } else {
            javaLogger.log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created but log level is turned off in the log");
            javaLogger.setLevel(Level.OFF);
        }
        return javaLogger;
    }
}
