
/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.utils;

import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.mya.catk.ConsentAccessToolKit;
import com.philips.platform.mya.catk.BuildConfig;

/**
 * Consent Widget loggger used for logginan system .
 * Can enable or desable logs and can be level of i,v,w etc.
 */
public class CatkLogger {

    private static boolean isLoggingEnabled;

    private static LoggingInterface mLoggingInterface;

    /**
     * Initialize the logger with AppInfra logger Taken care by USR coponent no need to call explicitly
     */
    public static void init() {
        mLoggingInterface = ConsentAccessToolKit.getInstance().getCatkComponent().getLoggingInterface();
        mLoggingInterface = mLoggingInterface.createInstanceForComponent("catk", BuildConfig.VERSION_NAME);
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

    private static void validateLoggerInitialization() {
        if (mLoggingInterface == null) {
            throw new RuntimeException("Please initiate Consent toolkit Logger by calling CatkLogger.init()");
        }
    }

    /**
     * Logs at debug level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void d(String tag, String message) {
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
    }

    /**
     * Logs at markErrorAndGetPrevious level
     *
     * @param tag     Tag
     * @param message Logging message
     */
    public static void e(String tag, String message) {
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
        validateLoggerInitialization();
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
    }

    @VisibleForTesting
    public static void setLoggerInterface(LoggingInterface loggingInterface) {
        mLoggingInterface = loggingInterface;
    }
}
