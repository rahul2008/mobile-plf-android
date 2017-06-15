package com.philips.amwelluapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class PTHMicroAppInterface implements UappInterface {
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
        PTHMicroAppLaunchInput PTHMicroAppLaunchInput = (PTHMicroAppLaunchInput) uappLaunchInput;
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, PTHLaunchActivity.class);
            intent.putExtra(WELCOME_MESSAGE, PTHMicroAppLaunchInput.getWelcomeMessage());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();
            PTHLaunchFragment PTHLaunchFragment = new PTHLaunchFragment();
            Bundle bundle = new Bundle();
            bundle.putString(WELCOME_MESSAGE, PTHMicroAppLaunchInput.getWelcomeMessage());
            PTHLaunchFragment.setArguments(bundle);
            PTHLaunchFragment.setFragmentLauncher(fragmentLauncher);
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), PTHLaunchFragment, PTHLaunchFragment.TAG);
            fragmentTransaction.addToBackStack(PTHLaunchFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
