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
    public void setScreenOrientation(ProductModelSelectionHelper.ActivityOrientation screenOrientation) {
        super.mScreenOrientation = screenOrientation;
    }

}
