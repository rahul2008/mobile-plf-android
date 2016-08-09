/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import android.os.Bundle;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 The purpose of the class if to launch microapp as activity
 */

public class ActivityLauncher extends UiLauncher {

    /**
     * These are Flags used for specifying screen orientation.
     * <p/>
     * <p> <b>Note : </b> The flags are similar to default android screen orientation flags</p>
     */

    @IntDef({ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,ActivityOrientation.SCREEN_ORIENTATION_LANDSCAPE, ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, ActivityOrientation.SCREEN_ORIENTATION_USER, ActivityOrientation.SCREEN_ORIENTATION_BEHIND, ActivityOrientation.SCREEN_ORIENTATION_SENSOR,
            ActivityOrientation.SCREEN_ORIENTATION_NOSENSOR, ActivityOrientation.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, ActivityOrientation.SCREEN_ORIENTATION_SENSOR_PORTRAIT, ActivityOrientation.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, ActivityOrientation.SCREEN_ORIENTATION_REVERSE_PORTRAIT,
            ActivityOrientation.SCREEN_ORIENTATION_FULL_SENSOR, ActivityOrientation.SCREEN_ORIENTATION_USER_LANDSCAPE, ActivityOrientation.SCREEN_ORIENTATION_USER_PORTRAIT, ActivityOrientation.SCREEN_ORIENTATION_FULL_USER, ActivityOrientation.SCREEN_ORIENTATION_LOCKED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActivityOrientation {
        public static final int SCREEN_ORIENTATION_UNSPECIFIED = -1;
        public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
        public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
        public static final int SCREEN_ORIENTATION_USER = 2;
        public static final int SCREEN_ORIENTATION_BEHIND = 3;
        public static final int SCREEN_ORIENTATION_SENSOR = 4;
        public static final int SCREEN_ORIENTATION_NOSENSOR = 5;
        public static final int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;
        public static final int SCREEN_ORIENTATION_SENSOR_PORTRAIT = 7;
        public static final int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
        public static final int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;
        public static final int SCREEN_ORIENTATION_FULL_SENSOR = 10;
        public static final int SCREEN_ORIENTATION_USER_LANDSCAPE = 11;
        public static final int SCREEN_ORIENTATION_USER_PORTRAIT = 12;
        public static final int SCREEN_ORIENTATION_FULL_USER = 13;
        public static final int SCREEN_ORIENTATION_LOCKED = 14;

    }



    /**
     Specific Screen orientation
     */
    protected ActivityLauncher.ActivityOrientation mScreenOrientation = null;

    /**
     Bundle object
     */
    protected Bundle mBundle;
    /**
     Launching As activity with Screen Orientation
     */
    public ActivityLauncher(ActivityLauncher.ActivityOrientation screenOrientation) {
        mScreenOrientation = screenOrientation;
    }

    /**
     Launching As activity with two parameters : Screen Orientation and Bundle
     */
    public ActivityLauncher(ActivityLauncher.ActivityOrientation screenOrientation, Bundle bundle) {
        mScreenOrientation = screenOrientation;
        mBundle = bundle;

    }
    /**
     returns screen orientation
     */
    public ActivityLauncher.ActivityOrientation getScreenOrientation() {
        return mScreenOrientation;
    }


    public Bundle getBundle() {
        return mBundle;
    }

}

