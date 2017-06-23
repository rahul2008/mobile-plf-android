package com.philips.cdp.devicepair.ui;

import android.content.Context;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

public interface LaunchFragmentViewListener {

    Context getActivityContext();

    FragmentLauncher getFragmentLauncher();
}
