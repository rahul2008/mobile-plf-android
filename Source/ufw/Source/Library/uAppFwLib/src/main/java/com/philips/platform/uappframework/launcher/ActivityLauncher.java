/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import android.os.Bundle;

import com.philips.platform.uid.thememanager.ThemeConfiguration;


/**
 The purpose of the class if to launch microapp as activity
 */

public class ActivityLauncher extends UiLauncher {


    private static final long serialVersionUID = -4985232973624436758L;

    /**
     * These are Flags used for specifying screen orientation.
     * <p/>
     * <p> <b>Note : </b> The flags are similar to default android screen orientation flags</p>
     */

    public enum ActivityOrientation {
        SCREEN_ORIENTATION_UNSPECIFIED(-1),
        SCREEN_ORIENTATION_LANDSCAPE(0),
        SCREEN_ORIENTATION_PORTRAIT(1),
        SCREEN_ORIENTATION_USER(2),
        SCREEN_ORIENTATION_BEHIND(3),
        SCREEN_ORIENTATION_SENSOR(4),
        SCREEN_ORIENTATION_NOSENSOR(5),
        SCREEN_ORIENTATION_SENSOR_LANDSCAPE(6),
        SCREEN_ORIENTATION_SENSOR_PORTRAIT(7),
        SCREEN_ORIENTATION_REVERSE_LANDSCAPE(8),
        SCREEN_ORIENTATION_REVERSE_PORTRAIT(9),
        SCREEN_ORIENTATION_FULL_SENSOR(10),
        SCREEN_ORIENTATION_USER_LANDSCAPE(11),
        SCREEN_ORIENTATION_USER_PORTRAIT(12),
        SCREEN_ORIENTATION_FULL_USER(13),
        SCREEN_ORIENTATION_LOCKED(14);

        private int value;

        ActivityOrientation(int value) {
            this.value = value;
        }
        /**
         To retrive Screen Orientation
         @return  Orientation value
         */

        public int getOrientationValue() {
            return this.value;
        }
    }
    protected static int mUiKitTheme;


    /**
     Specific Screen orientation
     */
    protected ActivityLauncher.ActivityOrientation mScreenOrientation = null;

    /**
     Bundle object
     */
    protected Bundle mBundle;

    private ThemeConfiguration mDLSThemeConfiguration;

    @Deprecated
    /**
     Constructor
     @param screenOrientation : takes screen Oreintation
     @param uikitTheme takes Uikit Theme


     */
    public ActivityLauncher(ActivityLauncher.ActivityOrientation screenOrientation ,int uikitTheme) {
        mScreenOrientation = screenOrientation;
        mUiKitTheme=uikitTheme;
    }

    @Deprecated
    /**
     Constructor
     @param screenOrientation : takes screen Orientation
     @param uikitTheme takes Uikit Theme
     @param bundle bundle object

     */
    public ActivityLauncher(ActivityLauncher.ActivityOrientation screenOrientation,int uikitTheme, Bundle bundle) {
        mScreenOrientation = screenOrientation;
        mBundle = bundle;
        mUiKitTheme=uikitTheme;

    }

    /**
     Constructor
     @param screenOrientation : takes screen Orientation
     @param dlsThemeConfiguration takes DLS configuration
     @param dlsUiKitTheme takes UiKit Theme
     @param bundle bundle object

     */
    public ActivityLauncher(ActivityLauncher.ActivityOrientation screenOrientation, ThemeConfiguration dlsThemeConfiguration,
                            int dlsUiKitTheme, Bundle bundle) {
        mScreenOrientation = screenOrientation;
        mBundle = bundle;
        mDLSThemeConfiguration =dlsThemeConfiguration;
        mUiKitTheme = dlsUiKitTheme;
    }
    /**
     @returns screen orientation
     */
    public ActivityLauncher.ActivityOrientation getScreenOrientation() {
        return mScreenOrientation;
    }
    /**
     @returns uikitTheme
     */
    public int getUiKitTheme() {
        return this.mUiKitTheme;
    }


    /**
     @returns Bundle
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public ThemeConfiguration getDlsThemeConfiguration() {
        return mDLSThemeConfiguration;
    }
}

