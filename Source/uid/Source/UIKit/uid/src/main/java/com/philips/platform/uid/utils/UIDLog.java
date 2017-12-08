/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.uid.utils;

import android.util.Log;
import com.philips.platform.uid.BuildConfig;

public final class UIDLog {
    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }
}
