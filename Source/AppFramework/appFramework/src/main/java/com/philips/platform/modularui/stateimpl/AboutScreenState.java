/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.aboutscreen.AboutScreenFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class AboutScreenState  extends UIState {
    AppFrameworkApplication appFrameworkApplication;


    public AboutScreenState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * Navigating to AboutScreenFragment
     * @param context requires context
     */
    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new AboutScreenFragment(), AboutScreenFragment.TAG);
    }

    /**
     * To handle back pressed
     * @param context requires context
     */
    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    @Override
    public void init(UiLauncher uiLauncher) {

    }
    @Override
    public void init(Context context) {

    }
}

