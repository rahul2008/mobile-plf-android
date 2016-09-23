/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class SettingsFragmentState extends UIState {

    private FragmentLauncher fragmentLauncher;
    /**
     * constructor
     * @param stateID
     */
    public SettingsFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * to navigate
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
            ((AppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                    showFragment(new SettingsFragment(), SettingsFragment.TAG,
                            getUiStateData().getFragmentAddState());

    }

    /**
     * to handle handleBack
     * @param context requires context
     */
    @Override
    public void handleBack(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    @Override
    public void init(Context context) {

    }
}
