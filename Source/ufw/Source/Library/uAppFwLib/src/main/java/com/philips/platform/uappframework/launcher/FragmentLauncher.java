/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework.launcher;


import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappframework.listener.ActionBarListener;

import java.io.Serializable;


public class FragmentLauncher extends UiLauncher implements Serializable {

    private static final long serialVersionUID = 3890701675982248919L;
    /**
        * Resource container ID. If you would like to launch uApp using fragment.
    */
    protected int mContainerResId;


   protected ActionBarListener mActionBarListener = null;

    /**
     * FragmentActivity context of your Fragment. If you would like to launch uApp using fragment.
     */
    protected transient FragmentActivity mFragmentActivity = null;

    /**
     * FragmentLauncher constructor for launching the uApp.
     * @param fragmentActivity Associated fragment Activity
     * @param containerResId The res Id of the container
     * @param actionBarListener instance of ActionBarListener
     */

    public FragmentLauncher(FragmentActivity fragmentActivity,
                            int containerResId,
                            ActionBarListener actionBarListener) {
        mContainerResId = containerResId;
        mActionBarListener = actionBarListener;
        mFragmentActivity = fragmentActivity;
    }

    /**
     * @return  fragment Container resource ID
     */
    public int getParentContainerResourceID() {
        return mContainerResId;
    }
    /**
     * @return actionBar listener object
     */
    public ActionBarListener getActionbarListener() {
        return mActionBarListener;
    }

    /**
     * @return associated fragment activity
     */
    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

}
