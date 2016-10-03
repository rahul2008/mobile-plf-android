/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * This class if used for dynamic configuration of Environment of User registration
 */
public class DebugTestFragmentState extends UIState {
    private FragmentLauncher fragmentLauncher;
    /**
     * constructor
     * @param stateID
     */
    public DebugTestFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * Navigate to the fragment
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new DebugTestFragment(), DebugTestFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }
}
