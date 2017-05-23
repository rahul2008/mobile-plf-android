package com.philips.cdp.productselection.launchertype;


/**

 * @author naveen@philips.com
 * @date 19/january/2016
 */
public class ActivityLauncher extends UiLauncher {

    protected ActivityOrientation mScreenOrientation = null;
    protected int mUiKitTheme;

    public ActivityLauncher(ActivityOrientation screenOrientation, int uiKitTheme) {
        mScreenOrientation = screenOrientation;
        this.mUiKitTheme = uiKitTheme;
    }

    public ActivityOrientation getScreenOrientation() {
        return mScreenOrientation;
    }

    public int getmUiKitTheme()
    {
        return mUiKitTheme;
    }

    /**
     * These are Flags used for setting/controlling screen orientation.
     * <p>This method helps only you are using the product selection component from the Intent</p>
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

        public int getOrientationValue() {
            return value;
        }
    }

}
