/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.util;

import com.philips.platform.appinfra.logging.LoggingInterface;

public class MYALog {
    // TODO: Deepthi, Remove the usages, use app infra logger directly  and this class if possible
    public final static String LOG = "mya";
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
