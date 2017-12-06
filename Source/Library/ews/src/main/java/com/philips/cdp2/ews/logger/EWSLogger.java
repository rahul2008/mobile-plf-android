/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.logger;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.platform.appinfra.logging.LoggingInterface;

@SuppressWarnings("unused")
public class EWSLogger {

    private EWSLogger() {
    }

    public static void v(String tag, String msg) {
        //getLoggerInterface().log(LoggingInterface.LogLevel.VERBOSE, tag, msg);
    }

    public static void d(String tag, String msg) {
        //getLoggerInterface().log(LoggingInterface.LogLevel.DEBUG, tag, msg);
    }

    public static void i(String tag, String msg) {
        //getLoggerInterface().log(LoggingInterface.LogLevel.INFO, tag, msg);
    }

    public static void w(String tag, String msg) {
        //getLoggerInterface().log(LoggingInterface.LogLevel.WARNING, tag, msg);
    }

    public static void e(String tag, String msg) {
        ///getLoggerInterface().log(LoggingInterface.LogLevel.ERROR, tag, msg);
    }

    private static LoggingInterface getLoggerInterface() {
        return EWSDependencyProvider.getInstance().getLoggerInterface();
    }
}