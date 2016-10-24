/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class HomeActivityState extends UIState {

    private FragmentLauncher fragmentLauncher;

    public HomeActivityState() {
        super(UIState.UI_HOME_STATE);
    }

    /**
     * Navigate to HomeActivity
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        fragmentLauncher.getFragmentActivity().startActivity(new Intent(fragmentLauncher.getFragmentActivity(), HomeActivity.class));
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public AppStates getStateEnum() {
        return AppStates.HOME;
    }
}
