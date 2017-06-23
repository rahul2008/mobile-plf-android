package com.philips.cdp.devicepair.uappdependencies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    public static String WELCOME_MESSAGE = "welcome_message";

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        WifiCommLibUappLaunchInput sampleMicroAppLaunchInput = (WifiCommLibUappLaunchInput) uappLaunchInput;
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, WifiCommLibUappLaunchActivity.class);
            intent.putExtra(WELCOME_MESSAGE, sampleMicroAppLaunchInput.getWelcomeMessage());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();
            LaunchFragment sampleFragment = new LaunchFragment();

            Bundle bundle = new Bundle();
            bundle.putString(WELCOME_MESSAGE, sampleMicroAppLaunchInput.getWelcomeMessage());
            sampleFragment.setArguments(bundle);
            sampleFragment.setFragmentLauncher(fragmentLauncher);

            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), sampleFragment, LaunchFragment.TAG);
            fragmentTransaction.addToBackStack(LaunchFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
