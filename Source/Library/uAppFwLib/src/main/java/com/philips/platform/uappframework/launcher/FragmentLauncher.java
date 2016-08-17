/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappframework.listener.ActionBarListener;


public class FragmentLauncher extends UiLauncher {
    /**
        * Resource container ID. If you would like to launch uApp using fragment.
    */
    protected int mContainerResId;


   protected ActionBarListener mActionBarListener = null;

    /**
     * FragmentActivity context of your Fragment. If you would like to launch uApp using fragment.
     */
    protected FragmentActivity mFragmentActivity = null;

    /**
     * FragmentLauncher method for launching the uApp.
     */

    public FragmentLauncher(FragmentActivity fragmentActivity,
                            int containerResId,
                            ActionBarListener actionBarListener) {
        mContainerResId = containerResId;
        mActionBarListener = actionBarListener;
        mFragmentActivity = fragmentActivity;
    }

    /**
     * Returns fragment Container resource ID
     */
    public int getParentContainerResourceID() {
        return mContainerResId;
    }
    /**
     * Returns actionBar listener object
     */
    public ActionBarListener getActionbarListener() {
        return mActionBarListener;
    }

    /**
     * Returns associated fragment activity
     */
    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

}
