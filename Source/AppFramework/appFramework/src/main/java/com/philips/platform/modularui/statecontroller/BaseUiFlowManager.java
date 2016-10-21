package com.philips.platform.modularui.statecontroller;

import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public abstract class BaseUiFlowManager {

    public abstract void initFlowManager();

    public abstract UIState getFirstState();

    public abstract void navigateToNextState(String eventID, UiLauncher uiLauncher);

    public abstract UIState getState(String eventID);
}
