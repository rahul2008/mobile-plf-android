/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private static final String TAG = LoggingExceptionHandler.class.getSimpleName();

    public LoggingExceptionHandler() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        SHNLogger.e(TAG, String.format("Uncaught exception: %s\nStack trace: %s\n", throwable.toString(), sw.toString()));
        // TODO TAG to supply feedback to dev team of what happened

        defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
    }
}
