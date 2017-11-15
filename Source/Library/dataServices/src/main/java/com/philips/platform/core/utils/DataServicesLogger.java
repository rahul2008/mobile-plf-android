package com.philips.platform.core.utils;

import android.util.Log;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dataservices.BuildConfig;

public class DataServicesLogger {

    private static boolean isLoggingEnabled;

    private static LoggingInterface mLoggingInterface;

    public static void init() {
        mLoggingInterface = DataServicesManager.getInstance().getAppInfra().getLogging().
                createInstanceForComponent(DataServicesConstants.COMPONENT_NAME, BuildConfig.VERSION_NAME);
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

    private static void validateLoggingInterface() {
        if (mLoggingInterface == null) {
            throw new RuntimeException("Please initiate logging via init");
        }
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
        validateLoggingInterface();
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG, tag, message);
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
        validateLoggingInterface();
        mLoggingInterface.log(LoggingInterface.LogLevel.ERROR, tag, message);
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
        }
        validateLoggingInterface();
        mLoggingInterface.log(LoggingInterface.LogLevel.INFO, tag, message);
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
        }
        validateLoggingInterface();
        mLoggingInterface.log(LoggingInterface.LogLevel.VERBOSE, tag, message);
    }
}
