/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.logger;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.logging.LoggingInterface;

@SuppressWarnings("unused")
public class EWSLogger {

    private LoggingInterface loggingInterface;

    public EWSLogger(@NonNull LoggingInterface loggingInterface) {
        this.loggingInterface = loggingInterface;
    }

    public  void v(String tag, String msg) {
        getLoggingInterface().log(LoggingInterface.LogLevel.VERBOSE, tag, msg);
    }

    public void d(String tag, String msg) {
        getLoggingInterface().log(LoggingInterface.LogLevel.DEBUG, tag, msg);
    }

    public void i(String tag, String msg) {
        getLoggingInterface().log(LoggingInterface.LogLevel.INFO, tag, msg);
    }

    public void w(String tag, String msg) {
        getLoggingInterface().log(LoggingInterface.LogLevel.WARNING, tag, msg);
    }

    public void e(String tag, String msg) {
        getLoggingInterface().log(LoggingInterface.LogLevel.ERROR, tag, msg);
    }

    private LoggingInterface getLoggingInterface() {
        return loggingInterface;
    }

}