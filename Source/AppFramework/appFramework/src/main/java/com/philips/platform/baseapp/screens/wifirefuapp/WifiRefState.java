package com.philips.platform.baseapp.screens.wifirefuapp;

import android.content.Context;

import com.philips.cdp.wifirefuapp.uappdependencies.WifiCommLibUappInterface;
import com.philips.cdp.wifirefuapp.uappdependencies.WifiCommLibUappLaunchInput;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 5/17/17.
 */

public class WifiRefState extends BaseState{

    private FragmentLauncher fragmentLauncher;
    private Context activityContext;

    public WifiRefState() {
        super(AppStates.WIFIREFUAPP);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher)uiLauncher;
        activityContext = fragmentLauncher.getFragmentActivity();
        launchWifiRefUApp();
    }

    private void launchWifiRefUApp() {
        WifiCommLibUappLaunchInput wifiCommLibUappLaunchInput = new WifiCommLibUappLaunchInput("Welcome Message");
        new WifiCommLibUappInterface().launch(fragmentLauncher,wifiCommLibUappLaunchInput);
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
