package com.philips.platform.samplemicroapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class SampleAppInterface implements UappInterface {
    private Context context;
    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        SampleAppDependencies sampleAppDependencies = (SampleAppDependencies) uappDependencies;
        this.context = sampleAppDependencies.getContext();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, SampleActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();
            SampleFragment sampleFragment = new SampleFragment();
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), sampleFragment, SampleFragment.TAG);
            fragmentTransaction.addToBackStack(SampleFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
