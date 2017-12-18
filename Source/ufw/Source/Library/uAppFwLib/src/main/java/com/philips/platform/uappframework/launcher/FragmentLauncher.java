/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * Needs to be instantiated for launching micro app as Fragment.
 * @since 1.0.0
 */

public class FragmentLauncher extends UiLauncher {

    protected int mContainerResId;


   protected ActionBarListener mActionBarListener = null;

    protected FragmentActivity mFragmentActivity = null;

    /**
     * FragmentLauncher constructor for launching the uApp.
     * @param fragmentActivity Associated fragment Activity
     * @param containerResId The res Id of the container
     * @param actionBarListener instance of ActionBarListener
     * @since 1.0.0
     */

    public FragmentLauncher(FragmentActivity fragmentActivity,
                            int containerResId,
                            ActionBarListener actionBarListener) {
        mContainerResId = containerResId;
        mActionBarListener = actionBarListener;
        mFragmentActivity = fragmentActivity;
    }

    /**
     * Get the resource ID of the parent container
     * @return  fragment Container resource ID
     * @since 1.0.0
     */
    public int getParentContainerResourceID() {
        return mContainerResId;
    }
    /**
     * Get the Action Bar listener object
     * @return actionBar listener object
     * @since 1.0.0
     */
    public ActionBarListener getActionbarListener() {
        return mActionBarListener;
    }

    /**
     * Get the associated fragment activity
     * @return associated fragment activity
     * @since 1.0.0
     */
    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

}
