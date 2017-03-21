/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.screens.homefragment;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.AppStates;
import com.philips.platform.screens.base.AppFrameworkBaseActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeFragmentState extends BaseState {

    public HomeFragmentState() {
        super(AppStates.HOME_FRAGMENT);
    }

    /**
     * for Navigation
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).handleFragmentBackStack( new HomeFragment(), HomeFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
