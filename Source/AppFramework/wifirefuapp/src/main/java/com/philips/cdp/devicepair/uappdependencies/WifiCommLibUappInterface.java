package com.philips.cdp.devicepair.uappdependencies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.devicepair.ui.LaunchFragment;
import com.philips.cdp.devicepair.ui.WifiCommLibUappLaunchActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class WifiCommLibUappInterface implements UappInterface {
    private Context context;

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
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
        Intent intent = new Intent(context, WifiCommLibUappLaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void launchAsFragment(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();
        LaunchFragment launchFragment = new LaunchFragment();
        launchFragment.setFragmentLauncher(fragmentLauncher);
        fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), launchFragment, LaunchFragment.TAG);
        fragmentTransaction.addToBackStack(LaunchFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
