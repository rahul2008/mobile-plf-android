package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;
import com.philips.cdp2.ews.view.EWSGettingStartedFragment;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;
import com.philips.cdp2.ews.view.EWSWiFiConnectFragment;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;

public class Navigator {

    @NonNull private final FragmentNavigator fragmentNavigator;

    public Navigator(@NonNull final FragmentNavigator fragmentNavigator) {
        this.fragmentNavigator = fragmentNavigator;
    }

    public void navigateToGettingStartedScreen() {
        fragmentNavigator.push(new EWSGettingStartedFragment(), R.id.contentFrame);
    }

    public void navigateToHomeNetworkConfirmationScreen() {
        fragmentNavigator.push(new EWSHomeWifiDisplayFragment(), R.id.contentFrame);
    }

    public void navigateToDevicePoweredOnConfirmationScreen() {
        fragmentNavigator.push(new EWSDevicePowerOnFragment(), R.id.contentFrame);
    }

    public void navigateToCompletingDeviceSetupScreen() {
        fragmentNavigator.push(new EWSPressPlayAndFollowSetupFragment(), R.id.contentFrame);
    }

    public void navigateToConnectToDeviceWithPasswordScreen() {
        fragmentNavigator.push(new EWSWiFiConnectFragment(), R.id.contentFrame);
    }

    public void navigateToPairingSuccessScreen() {
        fragmentNavigator.push(new EWSWiFiPairedFragment(), R.id.contentFrame);
    }

}