package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.splash.SplashFragment;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashState extends BaseState {

    public SplashState() {
        super(BaseAppState.SPLASH);
    }

    @Override
    public void navigate(final UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                addFragment(new SplashFragment(), SplashFragment.TAG);
    }

    @Override
    public void init(final Context context) {

    }
}
