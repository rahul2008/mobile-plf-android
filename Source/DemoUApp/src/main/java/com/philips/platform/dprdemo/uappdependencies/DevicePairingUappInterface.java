/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.uappdependencies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.dprdemo.DemoAppManager;
import com.philips.platform.dprdemo.ui.DevicePairingLaunchActivity;
import com.philips.platform.dprdemo.ui.LaunchFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class DevicePairingUappInterface implements UappInterface {
    private Context context;
    private DemoAppManager demoAppManager;

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        demoAppManager = DemoAppManager.getInstance();
        demoAppManager.initPreRequisite(uappSettings.getContext(),uappDependencies.getAppInfra());
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchAsActivity();
        } else {
            launchAsFragment(uiLauncher);
        }
    }

    private void launchAsActivity() {
        Intent intent = new Intent(context, DevicePairingLaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void launchAsFragment(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();

        LaunchFragment launchFragment = new LaunchFragment();
        fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), launchFragment, LaunchFragment.TAG);
        fragmentTransaction.addToBackStack(LaunchFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
