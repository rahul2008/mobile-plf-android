/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.homescreen.HomeFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeFragmentState extends UIState {
    /**
     * constructor
     * @param stateID
     */
    public HomeFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * for Navigation
     * @param context requires context
     */
    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new HomeFragment(), HomeFragment.TAG);
    }

    /**
     * to handle back
     * @param context requires context
     */
    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).finishActivity();
    }

    @Override
    public void init(UiLauncher uiLauncher) {

    }
    @Override
    public void init(Context context) {

    }
}
