package com.philips.platform.modularui.statecontroller;

import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface FragmentView extends UIView {

    ActionBarListener getActionBarListener();

    int getContainerId();
}
