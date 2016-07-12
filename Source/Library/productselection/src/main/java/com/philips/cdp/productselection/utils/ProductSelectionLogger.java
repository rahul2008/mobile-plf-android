package com.philips.cdp.productselection.utils;

import android.util.Log;


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
