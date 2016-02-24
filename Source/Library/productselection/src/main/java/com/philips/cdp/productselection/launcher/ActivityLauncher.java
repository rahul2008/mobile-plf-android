package com.philips.cdp.productselection.launcher;

import com.philips.cdp.productselection.ProductModelSelectionHelper;


/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Activity module.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public class ActivityLauncher extends UiLauncher {


    @Override
    public void setAnimation(int enterAnimation, int exitAnimation) {
        super.mEnterAnimation = enterAnimation;
        super.mExitAnimation = exitAnimation;
    }

    @Override
    public void setScreenOrientation(ActivityOrientation screenOrientation) {
        super.mScreenOrientation = screenOrientation;
    }



    /**
     * These are Flags used for setting/controlling screen orientation.
     * <p>This method helps only you are using the DigitalCare component from the Intent</p>
     * <p/>
     * <p> <b>Note : </b> The flags are similar to deafult android screen orientation flags</p>
     */
    public enum ActivityOrientation {


        SCREEN_ORIENTATION_UNSPECIFIED(-1), SCREEN_ORIENTATION_LANDSCAPE(0),
        SCREEN_ORIENTATION_PORTRAIT(1), SCREEN_ORIENTATION_USER(2), SCREEN_ORIENTATION_BEHIND(3),
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
