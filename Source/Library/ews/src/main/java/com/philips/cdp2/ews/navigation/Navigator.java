package com.philips.cdp2.ews.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiFragment;
import com.philips.cdp2.ews.hotspotconnection.ConnectingWithDeviceFragment;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordFragment;
import com.philips.cdp2.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.resetdevice.ResetDeviceTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WifiConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WrongWifiNetworkFragment;
import com.philips.cdp2.ews.confirmwifi.ConfirmWifiNetworkFragment;
import com.philips.cdp2.ews.connectionsuccessful.ConnectionSuccessfulFragment;
import com.philips.cdp2.ews.setupsteps.FirstSetupStepsFragment;
import com.philips.cdp2.ews.setupsteps.SecondSetupStepsFragment;
import com.philips.cdp2.ews.startconnectwithdevice.StartConnectWithDeviceFragment;

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
        pushFragment(new StartConnectWithDeviceFragment());
    }

    public void navigateToHomeNetworkConfirmationScreen() {
        pushFragment(new ConfirmWifiNetworkFragment());
    }

    public void navigateToDevicePoweredOnConfirmationScreen() {
        pushFragment(new FirstSetupStepsFragment());
    }

    public void navigateToCompletingDeviceSetupScreen() {
        pushFragment(new SecondSetupStepsFragment());
    }

    public void navigateToConnectToDeviceWithPasswordScreen(String friendlyName) {
        pushFragment(ConnectWithPasswordFragment.newInstance(friendlyName));
    }

    public void navigateToPairingSuccessScreen() {
        pushFragment(new ConnectionSuccessfulFragment());
    }

    public void navigateToResetConnectionTroubleShootingScreen() {
        pushFragment(new ResetConnectionTroubleshootingFragment());
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

    public void navigateToWIFIConnectionUnsuccessfulTroubleShootingScreen(@NonNull String deviceName, @NonNull String wifiSSID) {
        pushFragment(WifiConnectionUnsuccessfulFragment.newInstance(deviceName, wifiSSID));
    }

    public void navigateToConnectingPhoneToHotspotWifiScreen() {
        pushFragment(new ConnectingWithDeviceFragment());
    }

    public void navigateToConnectingDeviceWithWifiScreen(@NonNull final String homeWiFiSSID, @NonNull final String homeWiFiPassword, @NonNull final String deviceName, @NonNull final String deviceFriendlyName) {
        pushFragment(ConnectingDeviceWithWifiFragment.newInstance(homeWiFiSSID, homeWiFiPassword, deviceName, deviceFriendlyName));
    }

    public void navigateToConnectingDeviceWithWifiScreen(@Nullable Bundle bundle) {
        pushFragment(ConnectingDeviceWithWifiFragment.newInstance(bundle));
    }

    public void navigateToEWSWiFiPairedScreen() {
        pushFragment(new ConnectionSuccessfulFragment());
    }

    public void navigateToWrongWifiNetworkScreen(Bundle bundle) {
        pushFragment(WrongWifiNetworkFragment.newInstance(bundle));
    }

    public void navigateBack() {
        fragmentNavigator.pop();
    }

    private void pushFragment(@NonNull Fragment fragment) {
        fragmentNavigator.push(fragment,fragmentNavigator.getContainerId());
    }
}