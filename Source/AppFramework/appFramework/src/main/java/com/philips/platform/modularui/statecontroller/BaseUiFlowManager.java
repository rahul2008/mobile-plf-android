package com.philips.platform.modularui.statecontroller;

import android.content.Context;

import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public abstract class BaseUiFlowManager {

    private Context context;

    public BaseUiFlowManager(final Context context) {
        this.context = context;
    }

    public abstract void initFlowManager();

    public abstract UIState getFirstState();

    public abstract void navigateToNextState(String eventID, UiLauncher uiLauncher);

    public abstract UIState getState(String eventID);
}
