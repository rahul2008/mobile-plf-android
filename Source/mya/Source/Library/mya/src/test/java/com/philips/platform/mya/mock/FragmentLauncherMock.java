/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class FragmentLauncherMock extends FragmentLauncher {

    FragmentActivityMock fragmentActivity;

    public FragmentLauncherMock(FragmentActivityMock fragmentActivity, int containerResId, ActionBarListener actionBarListener) {
        super(fragmentActivity, containerResId, actionBarListener);
        this.fragmentActivity = fragmentActivity;
    }
}
