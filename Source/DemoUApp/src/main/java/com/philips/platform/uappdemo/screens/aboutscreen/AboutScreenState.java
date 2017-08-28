/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.aboutscreen;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.UappStates;
import com.philips.platform.uappdemo.screens.base.UappBaseActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;


/**
 * This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class AboutScreenState extends BaseState {

    FragmentLauncher fragmentLauncher;

    public AboutScreenState() {
        super(UappStates.ABOUT);
    }

    /**
     * Navigating to AboutScreenFragmentU
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((UappBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new AboutScreenFragmentU(), AboutScreenFragmentU.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}

