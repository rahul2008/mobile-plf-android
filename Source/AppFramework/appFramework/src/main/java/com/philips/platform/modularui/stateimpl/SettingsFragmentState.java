/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class SettingsFragmentState extends UIState {
    /**
     * constructor
     * @param stateID
     */
    public SettingsFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * to navigate
     * @param context requires context
     */
    @Override
    public void navigate(Context context) {
        if(context instanceof HomeActivity) {
            ((AppFrameworkBaseActivity) context).showFragment(new SettingsFragment(), SettingsFragment.TAG);
        }
    }

    /**
     * to handle back
     * @param context requires context
     */
    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    @Override
    public void init(UiLauncher uiLauncher) {

    }
}
