package com.philips.cdp.productselection.utils;

import android.util.Log;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.platform.appinfra.logging.LoggingInterface;


public class ProductSelectionLogger {

    public static final String ERROR = "Error"; // Use to log errors
    public static final String APPLICATION = "DigitalCareApp";
    public static final String ACTIVITY = "ActivityLifecycle";
    public static final String FRAGMENT = "FragmentLifecycle";
    public static final String DIGICAREACTIVITY = "ProductSelectionActivity";

    private static boolean isLoggingEnabled = true;

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
        if (isLoggingEnabled) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.DEBUG, tag, message + "");
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.ERROR, tag, message + "");
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.INFO, tag, message + "");
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.VERBOSE, tag, message + "");
        }
    }

    public static void w(String tag, String message) {
        if (isLoggingEnabled) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.WARNING, tag, message + "");
        }
    }
}
