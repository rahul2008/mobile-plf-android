/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.appinfra;


import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by 310238114 on 4/26/2016.
 */
public class LoggingWrapper extends AILogging {

    private final String mComponentID;
    private final String mComponentVersion;

    Logger javaLogger;

    public LoggingWrapper(AppInfra appInfra, String componentId, String componentVersion) {
        super(appInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;
        //javaLogger = LogManager.getLogManager().getLogger(componentId); // returns new or existing log
        javaLogger = Logger.getLogger(componentId); // returns new or existing log
        enableConsoleLog(true);
    }

    @Override
    public void log(LogLevel level, String eventId, String message) {
        //super.log(level, eventId, mComponentID, mComponentVersion, message);
        javaLogger.log(Level.INFO,eventId,message);  // java logger
    }


    public void enableConsoleLog(boolean isEnabled ) {
        javaLogger.setUseParentHandlers(true); //adds default console handler to app including other components
        if(isEnabled) { // Add console log
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new AIlogFormatter(mComponentID,mComponentVersion) );
            javaLogger.addHandler(consoleHandler);
            // mGobalLogger.setLevel(getCurrentLogLevel()); // default Level.ALL
        }else{ // remove console log
            Handler[] currentComponentHandlers = javaLogger.getHandlers();
            if (null!=currentComponentHandlers && currentComponentHandlers.length>0 ) {
                // file handler will be added at index1 after console handler at 0
                for (Handler handler : currentComponentHandlers) {
                    if (handler instanceof ConsoleHandler) {
                        handler.close(); // flush and close connection of file
                        javaLogger.removeHandler(handler);
                    }
                }
            }
        }
    }

}
