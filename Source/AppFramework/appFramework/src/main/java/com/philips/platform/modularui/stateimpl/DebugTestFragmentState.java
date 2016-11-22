/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * This class if used for dynamic configuration of Environment of User registration
 */
public class DebugTestFragmentState extends BaseState {

    public DebugTestFragmentState() {
        super(BaseAppState.DEBUG);
    }

    /**
     * Navigate to the fragment
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new DebugTestFragment(), DebugTestFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(Context context) {

    }
}
