package com.philips.platform.ths.uappclasses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.init.THSInitFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSPreWelcomeFragment;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;


public class THSMicroAppInterfaceImpl implements UappInterface {
    private Context context;
    private AppInfraInterface appInfra;
    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        appInfra = uappDependencies.getAppInfra();
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        THSMicroAppLaunchInput THSMicroAppLaunchInput = (THSMicroAppLaunchInput) uappLaunchInput;
        THSManager.getInstance().setAppInfra(appInfra);
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, THSLaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            FragmentTransaction fragmentTransaction = (fragmentLauncher.getFragmentActivity()).getSupportFragmentManager().beginTransaction();
            THSInitFragment welcomeFragment = new THSInitFragment();
            Bundle bundle = new Bundle();
            welcomeFragment.setArguments(bundle);
            welcomeFragment.setActionBarListener(fragmentLauncher.getActionbarListener());
            welcomeFragment.setFragmentLauncher(fragmentLauncher);
            fragmentTransaction.replace(fragmentLauncher.getParentContainerResourceID(), welcomeFragment, THSWelcomeFragment.TAG);
            fragmentTransaction.addToBackStack(THSWelcomeFragment.TAG);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
