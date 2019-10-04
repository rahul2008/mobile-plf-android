/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.utils;

import com.philips.platform.appinfra.logging.LoggingInterface;

public class MECLog {
    public final static String LOG = "mec";
    public static boolean isLoggingEnabled = false;
    public static LoggingInterface sAppLoggingInterface;

    public static void enableLogging(boolean enableLog) {
        isLoggingEnabled = enableLog;
    }
    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled && sAppLoggingInterface != null) {
            sAppLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled && sAppLoggingInterface != null) {
            sAppLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled && sAppLoggingInterface != null) {
            sAppLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled && sAppLoggingInterface != null) {
            sAppLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
        }
    }
}
