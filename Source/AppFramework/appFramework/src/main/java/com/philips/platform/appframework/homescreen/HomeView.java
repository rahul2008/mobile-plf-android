package com.philips.platform.appframework.homescreen;

import com.philips.platform.modularui.statecontroller.UIView;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface HomeView extends UIView {

    ActionBarListener getActionBarListener();

    int getContainerId();
}
