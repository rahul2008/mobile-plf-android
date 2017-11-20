package com.philips.cdp2.ews.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.confirmwifi.ConfirmWifiNetworkFragment;
import com.philips.cdp2.ews.connectionsuccessful.ConnectionSuccessfulFragment;
import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiFragment;
import com.philips.cdp2.ews.hotspotconnection.ConnectingWithDeviceFragment;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordFragment;
import com.philips.cdp2.ews.setupsteps.FirstSetupStepsFragment;
import com.philips.cdp2.ews.setupsteps.SecondSetupStepsFragment;
import com.philips.cdp2.ews.startconnectwithdevice.StartConnectWithDeviceFragment;
import com.philips.cdp2.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.resetdevice.ResetDeviceTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WifiConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WrongWifiNetworkFragment;

public class Navigator {

    @NonNull
    public FragmentNavigator getFragmentNavigator() {
        return fragmentNavigator;
    }

    @NonNull
    private final FragmentNavigator fragmentNavigator;

    public Navigator(@NonNull final FragmentNavigator fragmentNavigator) {
        this.fragmentNavigator = fragmentNavigator;
    }

    public void navigateToGettingStartedScreen() {
        pushFragment(new StartConnectWithDeviceFragment(), false);
    }

    public void navigateToHomeNetworkConfirmationScreen() {
        pushFragment(new ConfirmWifiNetworkFragment(), false);
    }

    public void navigateToDevicePoweredOnConfirmationScreen() {
        pushFragment(new FirstSetupStepsFragment(), false);
    }

    public void navigateToCompletingDeviceSetupScreen() {
        pushFragment(new SecondSetupStepsFragment(), false);
    }

    public void navigateToConnectToDeviceWithPasswordScreen(String friendlyName) {
        pushFragment(ConnectWithPasswordFragment.newInstance(friendlyName), true);
    }

    public void navigateToPairingSuccessScreen() {
        pushFragment(new ConnectionSuccessfulFragment(), false);
    }

    public void navigateToResetConnectionTroubleShootingScreen() {
        pushFragment(new ResetConnectionTroubleshootingFragment(), false);
    }

    public void navigateToResetDeviceTroubleShootingScreen() {
        pushFragment(new ResetDeviceTroubleshootingFragment(), false);
    }

    public void navigateToConnectToWrongPhoneTroubleShootingScreen() {
        pushFragment(new ConnectToWrongPhoneTroubleshootingFragment(), false);
    }

    public void navigateSetupAccessPointModeScreen() {
        pushFragment(new SetupAccessPointModeTroubleshootingFragment(), false);
    }

    public void navigateToWIFIConnectionUnsuccessfulTroubleShootingScreen(@NonNull String deviceName, @NonNull String wifiSSID) {
        pushFragment(WifiConnectionUnsuccessfulFragment.newInstance(deviceName, wifiSSID), true);
    }

    public void navigateToConnectingPhoneToHotspotWifiScreen() {
        pushFragment(new ConnectingWithDeviceFragment(), false);
    }

    public void navigateToConnectingDeviceWithWifiScreen(@NonNull final String homeWiFiSSID, @NonNull final String homeWiFiPassword, @NonNull final String deviceName, @NonNull final String deviceFriendlyName) {
        pushFragment(ConnectingDeviceWithWifiFragment.newInstance(homeWiFiSSID, homeWiFiPassword, deviceName, deviceFriendlyName), false);
    }

    public void navigateToConnectingDeviceWithWifiScreen(@Nullable Bundle bundle) {
        pushFragment(ConnectingDeviceWithWifiFragment.newInstance(bundle), false);
    }

    public void navigateToEWSWiFiPairedScreen() {
        pushFragment(new ConnectionSuccessfulFragment(), true);
    }

    public void navigateToWrongWifiNetworkScreen(Bundle bundle) {
        pushFragment(WrongWifiNetworkFragment.newInstance(bundle), true);
    }

    public void navigateBack() {
        fragmentNavigator.pop();
    }

    private void pushFragment(@NonNull Fragment fragment,boolean allowingStateLoss) {
        fragmentNavigator.push(fragment,fragmentNavigator.getContainerId(),allowingStateLoss);
    }
}