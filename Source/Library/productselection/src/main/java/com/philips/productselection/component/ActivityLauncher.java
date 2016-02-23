package com.philips.productselection.component;

import android.support.v4.app.FragmentActivity;

import com.philips.productselection.ProductModelSelectionHelper;
import com.philips.productselection.listeners.ActionbarUpdateListener;


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

    @Override
    public void setmLayoutResourceID(int mLayoutResourceID) {

    }


    @Override
    public void setActionbarUpdateListener(ActionbarUpdateListener mActionbarUpdateListener) {

    }

    @Override
    public void setFragmentActivity(FragmentActivity mFragmentActivity) {

    }
}
