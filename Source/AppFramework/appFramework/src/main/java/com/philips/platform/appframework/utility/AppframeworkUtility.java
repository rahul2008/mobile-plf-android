package com.philips.platform.appframework.utility;

import android.app.Activity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class AppframeworkUtility {

    public static boolean isActivityAlive(Activity activity) {
        return activity != null && !activity.isFinishing();
    }
}
