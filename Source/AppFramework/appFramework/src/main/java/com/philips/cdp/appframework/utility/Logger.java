/**
 * Custom log class: - Defines all log tags - Intercepts logs so more processing
 * is possible
 *
 * @author : Ritesh.jha@philips.com
 * @since: 31 May 2016
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.appframework.utility;

import android.util.Log;

public class Logger {

    private static boolean isLoggingEnabled = false;

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
            Log.d(tag, message + "");
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message + "");
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message + "");
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message + "");
        }
    }

    public static void w(String tag, String message) {
        if (isLoggingEnabled) {
            Log.w(tag, message + "");
        }
    }
}
