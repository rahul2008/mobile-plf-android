/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.datasevices.temperature.TemperatureTimeLineFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class DataSyncScreenState extends UIState {

    FragmentLauncher fragmentLauncher;

    public DataSyncScreenState() {
        super(UIState.UI_DATA_SYNC_SCREEN_STATE);
    }

    /**
     * Navigating to AboutScreenFragment
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new TemperatureTimeLineFragment(), TemperatureTimeLineFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }
}

