package com.philips.cdp;

import android.util.Log;

public class ProductLog {
    public static final String ONCLICK = "onClick";


    private static boolean isLoggingEnabled = true;
    public static void producrlog(String tag, String message) {
        if (isLoggingEnabled) {
            Log.d(tag, message);
        }
    }


}
