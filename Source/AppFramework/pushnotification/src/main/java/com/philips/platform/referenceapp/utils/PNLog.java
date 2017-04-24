package com.philips.platform.referenceapp.utils;

import android.util.Log;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * Created by philips on 19/04/17.
 */

public class PNLog {
    private static boolean isLoggingEnabled;
    private static LoggingInterface mLoggingInterface;

    public static void init(AppInfraInterface appInfra) {
        mLoggingInterface = appInfra.getLogging();
    }

    public static void enableLogging() {
        isLoggingEnabled = true;
    }

    public static void disableLogging() {
        isLoggingEnabled = false;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }


    public static void d(String tag, String message) {
        if(isLoggingEnabled) {
            Log.d(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
        }

    }

    public static void e(String tag, String message) {
        if(isLoggingEnabled) {
            Log.e(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
        }

    }

    public static void i(String tag, String message) {
        if(isLoggingEnabled) {
            Log.i(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
        }

    }

    public static void v(String tag, String message) {
        if(isLoggingEnabled) {
            Log.v(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
        }

    }
}
