package com.philips.cdp.productselection.utils;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.platform.appinfra.logging.LoggingInterface;


public class ProductSelectionLogger {

    public static final String ERROR = "Error"; // Use to log errors
    public static final String APPLICATION = "DigitalCareApp";
    public static final String ACTIVITY = "ActivityLifecycle";
    public static final String FRAGMENT = "FragmentLifecycle";
    public static final String DIGICAREACTIVITY = "ProductSelectionActivity";

    public static boolean isLoggingEnabled() {
        return (ProductModelSelectionHelper.getInstance().getLoggerInterface() != null);
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled()) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.DEBUG, tag, message + "");
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled()) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.ERROR, tag, message + "");
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled()) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.INFO, tag, message + "");
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled()) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.VERBOSE, tag, message + "");
        }
    }

    public static void w(String tag, String message) {
        if (isLoggingEnabled()) {
            ProductModelSelectionHelper.getInstance().getLoggerInterface().log(LoggingInterface.LogLevel.WARNING, tag, message + "");
        }
    }
}
