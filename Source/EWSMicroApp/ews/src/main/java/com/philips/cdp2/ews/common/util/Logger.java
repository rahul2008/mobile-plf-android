/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.BuildConfig;

public class Logger {

    private static final String SPACE = " ";
    private static final String NOT_IMPLEMENTED = "Not implemented";
    private static volatile LogInterface logger = null;

    static final int VERBOSE = 2;
    static final int DEBUG = 3;
    static final int INFO = 4;
    static final int WARN = 5;
    static final int ERROR = 6;

    public static void nopez(final String tag, final Object... params){
        warn(tag, printMessageWithParams(NOT_IMPLEMENTED, params));
    }

    public static void v(final String tag, final String message) {
        verbose(tag, message);
    }

    public static void v(final String tag, final String message, final Object... params) {
        verbose(tag, printMessageWithParams(message, params));
    }

    public static void d(final String tag, final String message) {
        debug(tag, message);
    }

    public static void d(final String tag, final String message, final Object... params) {
        debug(tag, printMessageWithParams(message, params));
    }

    public static void i(final String tag, final String message) {
        info(tag, message);
    }

    public static void i(final String tag, final String message, final Object... params) {
        info(tag, printMessageWithParams(message, params));
    }

    public static void w(final String tag, final String message) {
        warn(tag, message);
    }

    public static void w(final String tag, final String message, final Object... params) {
        warn(tag, printMessageWithParams(message, params));
    }

    public static void e(final String tag, final String message) {
        error(tag, message);
    }

    public static void e(final String tag, final String message, @NonNull final Throwable error) {
        error(tag, message + SPACE + error.getLocalizedMessage());
    }

    public static void e(final String tag, @NonNull final Throwable error) {
        error(tag, error.getLocalizedMessage());
    }

    public static void wtf(final String tag, final String message) {
        error(tag, message);
    }

    public static void wtf(final String tag, final String message, @NonNull final Throwable error) {
        error(tag, message + SPACE + error.getLocalizedMessage());
    }

    public static void wtf(final String tag, @NonNull final Throwable error) {
        error(tag, error.getLocalizedMessage());
    }

    private static void verbose(final String tag, final String message) {
        safeLog(VERBOSE, tag, message);
    }

    private static void debug(final String tag, final String message) {
        safeLog(DEBUG, tag, message);
    }

    private static void info(final String tag, final String message) {
        safeLog(INFO, tag, message);
    }

    private static void warn(final String tag, final String message) {
        safeLog(WARN, tag, message);
    }

    private static void error(final String tag, final String message) {
        safeLog(ERROR, tag, message);
    }


    @VisibleForTesting
    static String printMessageWithParams(String message, Object... params) {
        final String format = String.format(message, params);
        if (message.equals(format)) {
            StringBuilder sb = new StringBuilder(message);
            for (Object obj : params) {
                sb.append(SPACE).append(obj);
            }
            return sb.toString();
        } else {
            return format;
        }
    }

    private static void safeLog(final int lvl, final String tag, final String message) {
        if (logger != null && !BuildConfig.IS_PRODUCTION) {
            logger.log(lvl, tag, message);
        }
    }

    public static void setLogger(final LogInterface logInterface) {
        logger = logInterface;
    }
}
