package com.philips.cdp.wifirefuapp.ui;

import android.content.Context;

import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Created by philips on 6/8/17.
 */

public interface LaunchFragmentViewListener {

    Context getActivityContext();

    FragmentLauncher getFragmentLauncher();
}
