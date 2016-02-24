package com.philips.productselection.launcher;

import android.support.v4.app.FragmentActivity;

import com.philips.productselection.ProductModelSelectionHelper;
import com.philips.productselection.listeners.ActionbarUpdateListener;


/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Fragment module.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public class FragmentLauncher extends UiLauncher {


    @Override
    public void setAnimation(int enterAnimation, int exitAnimation) {
        super.mEnterAnimation = enterAnimation;
        super.mExitAnimation = exitAnimation;
    }

    public void setmLayoutResourceID(int layoutResourceID) {
        super.mLayoutResourceID = layoutResourceID;
    }

    public int getLayoutResourceID() {
        return mLayoutResourceID;
    }


    @Override
    public void setScreenOrientation(ProductModelSelectionHelper.ActivityOrientation mScreenOrientation) {

    }

    public void setActionbarUpdateListener(ActionbarUpdateListener actionbarUpdateListener) {
        super.mActionbarUpdateListener = actionbarUpdateListener;
    }


    public ActionbarUpdateListener getActionbarUpdateListener() {
        return mActionbarUpdateListener;
    }



    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        super.mFragmentActivity = fragmentActivity;

    }
}
