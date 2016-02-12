package com.philips.cdp.prxclient.Logger;

import android.util.Log;

/**
 * Description : This is the class used to enable debug mode.
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 05-Nov-15.
 */
public final class PrxLogger {

    private static boolean mEnabled = false;

    public static void enablePrxLogger(boolean enable) {
        mEnabled = enable;
    }

    public static void v(String tag, String message) {
        if (mEnabled)
            Log.v(tag, message);
    }

    public static void d(String tag, String message) {
        if (mEnabled)
            Log.d(tag, message);
    }

    public static void i(String tag, String message) {
        if (mEnabled)
            Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        if (mEnabled)
            Log.w(tag, message);
    }

    public static void e(String tag, String message) {
        if (mEnabled)
            Log.e(tag, message);
    }
}
