package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SplashState extends UIState {

    public SplashState() {
        super(UIState.UI_SPLASH_STATE);
    }

    @Override
    public void navigate(final UiLauncher uiLauncher) {

    }

    @Override
    public void init(final Context context) {

    }
}
