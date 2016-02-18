package com.philips.cdp.digitalcare.component;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;

/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Fragment call or the Activity Call.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public abstract class UiLauncher {

    /**
     * Enter {@Link android.view.animation} of the ConsumerCare Component Screens
     */
    protected int mEnterAnimation;


    /**
     * Exit {@Link android.view.animation} of the ConsumerCare Component Screens
     */
    protected int mExitAnimation;

    /**
     * Resource container ID. If you would like to add the the ConsumerCareModule to your application Fragment Manager.
     */
    protected int mLayoutResourceID;


    /**
     * Screen orientation control for the ConsumerCare Screens
     */
    protected DigitalCareConfigManager.ActivityOrientation mScreenOrientation = null;


    /**
     * Actionbar listers for the applications with Fragments.
     */
    protected ActionbarUpdateListener mActionbarUpdateListener = null;


    /**
     * FragmentActivity context of your Fragment. If you would like to add the the ConsumerCareModule to your application Fragment Manager.
     */
    protected FragmentActivity mFragmentActivity = null;


    public int getEnterAnimation() {
        return mEnterAnimation;
    }

    public abstract void setAnimation(int mEnterAnimation, int mExitAnimation);

    public int getExitAnimation() {
        return mExitAnimation;
    }

    public int getLayoutResourceID() {
        return mLayoutResourceID;
    }

    public abstract void setmLayoutResourceID(int mLayoutResourceID);

    public DigitalCareConfigManager.ActivityOrientation getScreenOrientation() {
        return mScreenOrientation;
    }

    public abstract void setScreenOrientation(DigitalCareConfigManager.ActivityOrientation mScreenOrientation);

    public ActionbarUpdateListener getActionbarUpdateListener() {
        return mActionbarUpdateListener;
    }

    public abstract void setActionbarUpdateListener(ActionbarUpdateListener mActionbarUpdateListener);

    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

    public abstract void setFragmentActivity(FragmentActivity mFragmentActivity);
}
