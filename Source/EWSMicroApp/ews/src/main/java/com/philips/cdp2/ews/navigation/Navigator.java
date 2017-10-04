package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;
import com.philips.cdp2.ews.view.EWSGettingStartedFragment;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;
import com.philips.cdp2.ews.view.EWSWiFiConnectFragment;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;
import com.philips.cdp2.ews.view.TroubleshootHomeWiFiFragment;

public class Navigator {

    @NonNull private final FragmentNavigator fragmentNavigator;

    public Navigator(@NonNull final FragmentNavigator fragmentNavigator) {
        this.fragmentNavigator = fragmentNavigator;
    }

    public void navigateToGettingStartedScreen() {
        pushFragment(new EWSGettingStartedFragment());
    }

    public void navigateToHomeNetworkConfirmationScreen() {
        pushFragment(new EWSHomeWifiDisplayFragment());
    }

    public void navigateToDevicePoweredOnConfirmationScreen() {
        pushFragment(new EWSDevicePowerOnFragment());
    }

    public void navigateToCompletingDeviceSetupScreen() {
        pushFragment(new EWSPressPlayAndFollowSetupFragment());
    }

    public void navigateToConnectToDeviceWithPasswordScreen() {
        pushFragment(new EWSWiFiConnectFragment());
    }

    public void navigateToPairingSuccessScreen() {
        pushFragment(new EWSWiFiPairedFragment());
    }

    public void navigateToWifiTroubleShootingScreen() {
        pushFragment(new TroubleshootHomeWiFiFragment());
    }

    private void pushFragment(@NonNull Fragment fragment) {
        fragmentNavigator.push(fragment, R.id.contentFrame);
    }

}