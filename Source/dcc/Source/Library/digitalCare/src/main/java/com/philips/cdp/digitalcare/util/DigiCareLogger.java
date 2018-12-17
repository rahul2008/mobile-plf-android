/**
 * Custom log class: - Defines all log tags - Intercepts logs so more processing
 * is possible
 *
 * @author : Ritesh.jha@philips.com
 * @since: 5 Dec 2015
 * <p>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.util;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.platform.appinfra.logging.LoggingInterface;


public class DigiCareLogger {

    public static final String ERROR = "Error"; // Use to log errors
    public static final String APPLICATION = "DigitalCareApp";
    public static final String ACTIVITY = "ActivityLifecycle";
    public static final String FRAGMENT = "FragmentLifecycle";
    public static final String DIGICAREACTIVITY = "DigitalCareActivity";

    public static boolean isLoggingSupported() {
        return (getLoggerInterface() != null);
    }

    public static void d(String tag, String message) {
        if (isLoggingSupported()) {
            getLoggerInterface().log(LoggingInterface.
                    LogLevel.DEBUG, tag, message + "");
        }
    }

    public static LoggingInterface getLoggerInterface() {
        return DigitalCareConfigManager.getInstance().getLoggerInterface();
    }

    public static void e(String tag, String message) {
        if (isLoggingSupported()) {
            getLoggerInterface().log(LoggingInterface.
                    LogLevel.ERROR, tag, message + "");
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingSupported()) {
            getLoggerInterface().log(LoggingInterface.
                    LogLevel.INFO, tag, message + "");
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingSupported()) {
            getLoggerInterface().log(LoggingInterface.
                    LogLevel.VERBOSE, tag, message + "");
        }
    }

    public static void w(String tag, String message) {
        if (isLoggingSupported()) {
            getLoggerInterface().log(
                    LoggingInterface.LogLevel.WARNING, tag, message + "");
        }
    }
}
