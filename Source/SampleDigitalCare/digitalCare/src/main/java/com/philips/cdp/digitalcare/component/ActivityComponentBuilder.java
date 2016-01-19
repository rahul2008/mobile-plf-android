package com.philips.cdp.digitalcare.component;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;

/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Activity module.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public class ActivityComponentBuilder extends ComponentBuilder {

    @Override
    public void setEnterAnimation(int enterAnimation) {
        super.mEnterAnimation = enterAnimation;
    }

    @Override
    public void setExitAnimation(int exitAnimation) {
        super.mExitAnimation = exitAnimation;
    }

    @Override
    public void setScreenOrientation(DigitalCareConfigManager.ActivityOrientation screenOrientation) {
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
