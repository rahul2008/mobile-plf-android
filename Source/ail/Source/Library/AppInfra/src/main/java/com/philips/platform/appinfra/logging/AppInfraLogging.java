/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;


import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraLogEventID;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class AppInfraLogging implements LoggingInterface {


    private static final long serialVersionUID = -4898715486015827285L;
    private AppInfra mAppInfra;
    private transient Logger mJavaLogger;
    String mComponentID="";
    String mComponentVersion="";

    public AppInfraLogging(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }


    @Override
    public LoggingInterface createInstanceForComponent(String componentId, String componentVersion) {
        mComponentID = componentId;
        mComponentVersion = componentVersion;
        return new LoggingWrapper(mAppInfra, mComponentID, mComponentVersion);
    }


    @Override
    public void log(LogLevel level, String eventId, String message) {
        // native Java logger mapping of LOG levels
        if (null == mJavaLogger) {
            createLogger("","");
        }
        if (null != mJavaLogger) {
            switch (level) {
                case ERROR:
                    getJavaLogger().log(Level.SEVERE, eventId, message);
                    break;
                case WARNING:
                    getJavaLogger().log(Level.WARNING, eventId, message);
                    break;
                case INFO:
                    getJavaLogger().log(Level.INFO, eventId, message);
                    break;
                case DEBUG:
                    getJavaLogger().log(Level.CONFIG, eventId, message);
                    break;
                case VERBOSE:
                    getJavaLogger().log(Level.FINE, eventId, message);
                    break;
            }
        }

    }

    /**
     * Logs message on console and file .
     *
     * @param level   the level {VERBOSE, DEBUG, INFO, WARNING, ERROR}
     * @param eventId the Event name or Tag
     * @param message the message
     * @param map the dictionary
     * @since 1.0.0
     */
    @Override
    public void log(LogLevel level, String eventId, String message, Map<String, ?> map) {
        Object[] params = getParamObjects();

        // native Java logger mapping of LOG levels
        if (null == mJavaLogger) {
            createLogger("","");
        }
        if (null != mJavaLogger) {
            params[0]=message;
            params[1]=map;
            switch (level) {
                case ERROR:
                    getJavaLogger().log(Level.SEVERE, eventId, params);
                    break;
                case WARNING:
                    getJavaLogger().log(Level.WARNING, eventId, params);
                    break;
                case INFO:
                    getJavaLogger().log(Level.INFO, eventId, params);
                    break;
                case DEBUG:
                    getJavaLogger().log(Level.CONFIG, eventId, params);
                    break;
                case VERBOSE:
                    getJavaLogger().log(Level.FINE, eventId, params);
                    break;
            }
        }
    }

    @NonNull
    Object[] getParamObjects() {
        return new Object[2];
    }

    void createLogger(String pComponentId, String pComponentVersion) {
        final LoggingConfiguration loggingConfiguration = new LoggingConfiguration(mAppInfra, pComponentId, pComponentVersion);
        final HashMap<?, ?> loggingProperty = loggingConfiguration.getLoggingProperties(mAppInfra);
        if (null != loggingProperty) {
            mJavaLogger = loggingConfiguration.getLoggerBasedOnConfig(pComponentId, loggingProperty);
            getJavaLogger().log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created"); //R-AI-LOG-6
        } else {
                 /* added just to make unit test cases pass */
            mJavaLogger = loggingConfiguration.getLogger(pComponentId); // returns new or existing log
            getJavaLogger().log(Level.INFO, AppInfraLogEventID.AI_LOGGING + "Logger created"); //R-AI-LOG-6
        }
    }

    protected Logger getJavaLogger() {
        return mJavaLogger;
    }

}
