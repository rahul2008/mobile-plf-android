/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */


package com.philips.platform.ths.utility;

import android.util.Log;

import com.philips.platform.appinfra.logging.LoggingInterface;

public class AmwellLog {
    public final static String LOG = "Amwell";
    public static boolean isLoggingEnabled = false;

    public static void enableLogging(boolean enableLog) {
        isLoggingEnabled = enableLog;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
           // Log.d(tag, message);
            THSManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG,tag,message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            //Log.e(tag, message);
            THSManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.ERROR,tag,message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            //Log.i(tag, message);
            THSManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.INFO,tag,message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            //Log.v(tag, message);
            THSManager.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.VERBOSE,tag,message);
        }
    }
}