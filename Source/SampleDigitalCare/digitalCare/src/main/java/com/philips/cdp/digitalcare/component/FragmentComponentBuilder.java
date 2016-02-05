package com.philips.cdp.digitalcare.component;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;

/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Fragment module.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public class FragmentComponentBuilder extends ComponentBuilder {


    @Override
    public void setEnterAnimation(int enterAnimation) {
        super.mEnterAnimation = enterAnimation;
    }

    @Override
    public void setExitAnimation(int exitAnimation) {
        super.mExitAnimation = exitAnimation;
    }

    @Override
    public void setmLayoutResourceID(int layoutResourceID) {
        super.mLayoutResourceID = layoutResourceID;
    }

    @Override
    public void setScreenOrientation(DigitalCareConfigManager.ActivityOrientation mScreenOrientation) {

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
