package com.philips.platform.referenceapp.utils;

import android.util.Log;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

/**
 * Created by philips on 19/04/17.
 */

public class PNLog {
    private static boolean loggingEnabled;
    private static LoggingInterface mLoggingInterface;

    public static void initialise(AppInfraInterface appInfra) {
        mLoggingInterface = appInfra.getLogging();
    }

    public static void enablePNLogging() {
        loggingEnabled = true;
    }

    public static void disablePNLogging() {
        loggingEnabled = false;
    }

    public static boolean isPNLoggingEnabled() {
        return loggingEnabled;
    }


    public static void d(String tag, String message) {
        if(loggingEnabled) {
            Log.d(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
        }

    }

    public static void e(String tag, String message) {
        if(loggingEnabled) {
            Log.e(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
        }

    }

    public static void i(String tag, String message) {
        if(loggingEnabled) {
            Log.i(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
        }

    }

    public static void v(String tag, String message) {
        if(loggingEnabled) {
            Log.v(tag, message);
            mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
        }

    }
}
