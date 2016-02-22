package com.philips.cdp.digitalcare.component;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;
import com.philips.multiproduct.ProductModelSelectionHelper;

/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Fragment module.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public class FragmentBuilder extends UiLauncher {

    public FragmentBuilder(ProductModelSelectionHelper.ActivityOrientation screenOrientation) {
        super.mScreenOrientation = screenOrientation;
    }

    @Override
    public void setAnimation(int enterAnimation, int exitAnimation) {
        super.mEnterAnimation = enterAnimation;
        super.mExitAnimation = exitAnimation;
    }
    @Override
    public void setmLayoutResourceID(int layoutResourceID) {
        super.mLayoutResourceID = layoutResourceID;
    }

    @Override
    public void setScreenOrientation(ProductModelSelectionHelper.ActivityOrientation mScreenOrientation) {

    }

    @Override
    public void setActionbarUpdateListener(ActionbarUpdateListener actionbarUpdateListener) {
        super.mActionbarUpdateListener = actionbarUpdateListener;
    }

    @Override
    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        super.mFragmentActivity = fragmentActivity;

    }
}
