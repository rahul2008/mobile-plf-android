package com.philips.platform.baseapp.screens.wifirefuapp;

import android.content.Context;

import com.philips.platform.referenceapp.uappdependencies.WifiCommLibUappInterface;
import com.philips.platform.referenceapp.uappdependencies.WifiCommLibUappLaunchInput;
import com.philips.platform.referenceapp.ui.LaunchFragment;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

public class WifiRefState extends BaseState {

    private FragmentLauncher fragmentLauncher;
    private Context activityContext;

    public WifiRefState() {
        super(AppStates.WIFIREFUAPP);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        activityContext = fragmentLauncher.getFragmentActivity();
        launchWifiRefUApp();
    }

    private void launchWifiRefUApp() {
        WifiCommLibUappLaunchInput wifiCommLibUappLaunchInput = new WifiCommLibUappLaunchInput("Welcome Message");
        new WifiCommLibUappInterface().launch(fragmentLauncher, wifiCommLibUappLaunchInput);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
