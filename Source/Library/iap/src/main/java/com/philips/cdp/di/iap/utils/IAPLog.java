/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.util.Log;

import com.philips.cdp.di.iap.integration.IAPDependencies;

public class IAPLog {
    public final static String LOG = "InAppPurchase";
    public static boolean isLoggingEnabled = false;

    public static void enableLogging(boolean enableLog) {
        isLoggingEnabled = enableLog;
    }

    public static boolean isLoggingEnabled() {
        return isLoggingEnabled;
    }

    public static void d(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (isLoggingEnabled) {
            Log.e(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (isLoggingEnabled) {
            Log.i(tag, message);
        }
    }

    public static void v(String tag, String message) {
        if (isLoggingEnabled) {
            Log.v(tag, message);
        }
    }
}
