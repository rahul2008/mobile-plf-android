package com.philips.cdp2.ews.navigation;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.hotspotconnection.ConnectingPhoneToHotspotWifiFragment;
import com.philips.cdp2.ews.troubleshooting.connectionfailure.ConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.view.ConnectToWrongPhoneTroubleshootingFragment;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;
import com.philips.cdp2.ews.view.EWSGettingStartedFragment;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;
import com.philips.cdp2.ews.view.EWSWiFiConnectFragment;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;
import com.philips.cdp2.ews.view.ResetConnectionTroubleshootingFragment;
import com.philips.cdp2.ews.view.ResetDeviceTroubleshootingFragment;
import com.philips.cdp2.ews.view.SetupAccessPointModeTroubleshootingFragment;
import com.philips.cdp2.ews.view.TroubleshootConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.troubleshooting.homewifi.TroubleshootHomeWiFiFragment;

public class Navigator {

    @NonNull private final FragmentNavigator fragmentNavigator;
    @NonNull private final ActivityNavigator activityNavigator;

    public Navigator(@NonNull final FragmentNavigator fragmentNavigator, @NonNull final ActivityNavigator activityNavigator) {
        this.fragmentNavigator = fragmentNavigator;
        this.activityNavigator = activityNavigator;
    }

    public void navigateToGettingStartedScreen() {
        pushFragment(new EWSGettingStartedFragment());
    }

    public void navigateToHomeNetworkConfirmationScreen() {
        pushFragment(new EWSHomeWifiDisplayFragment());
    }

    public void navigateToDevicePoweredOnConfirmationScreen() {
        boolean isPresentInStack = fragmentNavigator.popToFragment(EWSDevicePowerOnFragment.class.getCanonicalName());
        if (!isPresentInStack) {
            pushFragment(new EWSDevicePowerOnFragment());
        }
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
        activityNavigator.showFragment(TroubleshootHomeWiFiFragment.class.getCanonicalName());
    }

    public void navigateToResetConnectionTroubleShootingScreen() {
        boolean isPresentInStack = fragmentNavigator.popToFragment(ResetConnectionTroubleshootingFragment.class.getCanonicalName());
        if (!isPresentInStack) {
            pushFragment(new ResetConnectionTroubleshootingFragment());
        }
    }

    public void navigateToResetDeviceTroubleShootingScreen() {
        pushFragment(new ResetDeviceTroubleshootingFragment());
    }

    public void navigateToConnectToWrongPhoneTroubleShootingScreen() {
        pushFragment(new ConnectToWrongPhoneTroubleshootingFragment());
    }

    public void navigateSetupAccessPointModeScreen() {
        pushFragment(new SetupAccessPointModeTroubleshootingFragment());
    }

    public void navigateToConnectionUnsuccessfulTroubleShootingScreen() {
        pushFragment(new TroubleshootConnectionUnsuccessfulFragment());
    }

    public void navigateToConnectingPhoneToHotspotWifiScreen() {
        pushFragment(new ConnectingPhoneToHotspotWifiFragment());
    }

    public void navigateToUnsuccessfulConnectionDialog(@NonNull Fragment currentFragment, int requestCode) {
        activityNavigator.showFragmentWithResult(currentFragment,
                ConnectionUnsuccessfulFragment.class.getCanonicalName(), requestCode);
    }

    private void pushFragment(@NonNull Fragment fragment) {
        fragmentNavigator.push(fragment, R.id.contentFrame);
    }

}