package com.philips.platform.appinfra.logging;

import androidx.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by abhishek on 4/24/18.
 */

public class ConsoleLogConfigurationHandler {

    private AppInfraInterface appInfra;


    public ConsoleLogConfigurationHandler(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    protected void handleConsoleLogConfig(LogFormatter logFormatter, LoggingConfiguration loggingConfiguration, @NonNull Logger logger){
        boolean isEnabled=loggingConfiguration.isConsoleLogEnabled();
        if (isEnabled) {
            ConsoleHandler consoleHandler = getCurrentLogConsoleHandler(logger);
            if (null == consoleHandler) {
                consoleHandler = new ConsoleHandler();
                if (null != logger.getLevel()) {
                    consoleHandler.setLevel(logger.getLevel());
                } else {
                    // for appinfra internal log mJavaLogger will be null
                    consoleHandler.setLevel(Level.FINE);
                }
                consoleHandler.setFormatter(logFormatter);
                // mConsoleHandler.setFilter(new LogFilter(null,"ev1"));
                logger.addHandler(consoleHandler);
            }else{
                //TODO:Need to handle this case.
            }

        } else { // remove console log if any
            final Handler[] currentComponentHandlers =logger.getHandlers();
            if (null != currentComponentHandlers && currentComponentHandlers.length > 0) {
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        handler.close(); // flush and close connection of file
                        logger.removeHandler(handler);
                    }
                }
            }
        }
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
