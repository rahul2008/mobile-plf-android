package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.connectivity.ConnectivityFragment;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityFragmentState extends UIState{
    private FragmentLauncher fragmentLauncher;
    public ConnectivityFragmentState() {
        super(UIState.UI_DEBUG_FRAGMENT_STATE);
    }

    @Override
    public void navigate(final UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity)fragmentLauncher.getFragmentActivity()).
                handleFragmentBackStack( new ConnectivityFragment(), ConnectivityFragment.TAG,getUiStateData().getFragmentLaunchState());
    }

    @Override
    public void init(final Context context) {

    }
}
