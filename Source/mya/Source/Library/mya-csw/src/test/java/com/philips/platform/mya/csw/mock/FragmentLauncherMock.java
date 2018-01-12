package com.philips.platform.mya.csw.mock;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class FragmentLauncherMock extends FragmentLauncher {

    FragmentActivityMock fragmentActivity;

    public FragmentLauncherMock(FragmentActivityMock fragmentActivity, int containerResId, ActionBarListener actionBarListener) {
        super(fragmentActivity, containerResId, actionBarListener);
        this.fragmentActivity = fragmentActivity;
    }
}
