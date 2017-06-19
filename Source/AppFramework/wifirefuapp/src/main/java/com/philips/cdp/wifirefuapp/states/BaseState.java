package com.philips.cdp.wifirefuapp.states;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

public abstract class BaseState {
    FragmentLauncher context;

    public BaseState(FragmentLauncher context) {
        this.context = context;
    }

    abstract void start(StateContext stateContext);
}
