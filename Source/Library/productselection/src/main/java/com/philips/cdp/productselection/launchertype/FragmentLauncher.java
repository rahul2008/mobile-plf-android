package com.philips.cdp.productselection.launchertype;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;


/**
 * Description:  This class responsible for providing the builderclass to invoke the consumerCare module as
 * Fragment module.
 *
 * @author naveen@philips.com
 * @date 19/january/2015
 */
public class FragmentLauncher extends UiLauncher {

    /**
     * Resource container ID. If you would like to add the the ConsumerCareModule to your application Fragment Manager.
     */
    protected int mParentContainerResourceID;


    protected ActionbarUpdateListener mActionbarUpdateListener = null;

    /**
     * FragmentActivity context of your Fragment. If you would like to add the the ConsumerCareModule to your application Fragment Manager.
     */
    protected FragmentActivity mFragmentActivity = null;


    public FragmentLauncher(FragmentActivity fragmentActivity,
                            int parentContainerResId,
                            ActionbarUpdateListener actionbarUpdateListener) {
        mParentContainerResourceID = parentContainerResId;
        mActionbarUpdateListener = actionbarUpdateListener;
        mFragmentActivity = fragmentActivity;
    }


    public int getLayoutResourceID() {
        return mParentContainerResourceID;
    }

    public ActionbarUpdateListener getActionbarUpdateListener() {
        return mActionbarUpdateListener;
    }


    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

}
