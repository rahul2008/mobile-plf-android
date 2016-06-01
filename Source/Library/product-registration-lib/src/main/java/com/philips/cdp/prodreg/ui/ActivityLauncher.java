package com.philips.cdp.prodreg.ui;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActivityLauncher extends UiLauncher {
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

        public int getOrientationValue() {
            return this.value;
        }
    }

    protected ActivityLauncher.ActivityOrientation mScreenOrientation = null;
    protected int mUiKitTheme;

    public ActivityLauncher(ActivityLauncher.ActivityOrientation screenOrientation, int uiKitTheme) {
        this.mScreenOrientation = screenOrientation;
        this.mUiKitTheme = uiKitTheme;
    }

    public ActivityLauncher.ActivityOrientation getScreenOrientation() {
        return this.mScreenOrientation;
    }

    public int getmUiKitTheme() {
        return this.mUiKitTheme;
    }
}
