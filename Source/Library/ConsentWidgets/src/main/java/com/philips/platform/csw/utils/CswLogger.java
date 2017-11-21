/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.utils;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.mya.consentwidgets.BuildConfig;

/**
 * Consent Widget loggger used for logginan system .
 * Can enable or desable logs and can be level of i,v,w etc.
 */
public class CswLogger {

    private static boolean isLoggingEnabled;

    private static LoggingInterface mLoggingInterface;

    /**
     * Initialize the logger with AppInfra logger Taken care by USR coponent no need to call explicitly
     */
    public static void init() {
        mLoggingInterface = CswInterface.getCswComponent().getLoggingInterface();
        mLoggingInterface = mLoggingInterface.createInstanceForComponent("csw", BuildConfig.VERSION_NAME);
    }

    /**
     * Enable logging
     */
    public static void enableLogging() {
        isLoggingEnabled = true;
    }

    /**
     * Disable logging
     */
    public static void disableLogging() {
        isLoggingEnabled = false;
    }

    /**
     * Status of logs enabled or dissabled
     *
     * @return true if enabled else false.
     */
    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void validateLoggerInitialization() {
        if (mLoggingInterface == null) {
            throw new RuntimeException("Please initiate Consent widget Logger by calling CswLogger.init()");
        }
    }

    /**
     * Logs at debug level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
    }

    /**
     * Logs at error level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
    }

    /**
     * Logs at info level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
    }

    /**
     * Logs at verbose level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
        }
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
    }

    @VisibleForTesting
    public static void setLoogerInterface(LoggingInterface loggingInterface) {
        mLoggingInterface = loggingInterface;
    }
}
