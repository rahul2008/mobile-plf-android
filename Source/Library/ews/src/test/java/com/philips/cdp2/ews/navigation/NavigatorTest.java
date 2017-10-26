package com.philips.cdp2.ews.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiFragment;
import com.philips.cdp2.ews.hotspotconnection.ConnectingPhoneToHotspotWifiFragment;
import com.philips.cdp2.ews.settingdeviceinfo.SetDeviceInfoFragment;
import com.philips.cdp2.ews.troubleshooting.connecttowrongphone.ConnectToWrongPhoneTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.resetconnection.ResetConnectionTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.resetdevice.ResetDeviceTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.setupaccesspointmode.SetupAccessPointModeTroubleshootingFragment;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WifiConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.troubleshooting.wificonnectionfailure.WrongWifiNetworkFragment;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;
import com.philips.cdp2.ews.view.EWSGettingStartedFragment;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NavigatorTest {

    @InjectMocks
    private Navigator subject;

    @Mock
    private FragmentNavigator mockFragmentNavigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldPushGettingStartingFragmentWhenNavigating() throws Exception {
        subject.navigateToGettingStartedScreen();

        verifyFragmentPushed(EWSGettingStartedFragment.class);
    }

    @Test
    public void itShouldPushHomeNetworkConfirmationScreenWhenNavigating() throws Exception {
        subject.navigateToHomeNetworkConfirmationScreen();

        verifyFragmentPushed(EWSHomeWifiDisplayFragment.class);
    }

    @Test
    public void itShouldPushDevicePoweredOnConfirmationScreenWhenNavigating() throws Exception {
        subject.navigateToDevicePoweredOnConfirmationScreen();

        verifyFragmentPushed(EWSDevicePowerOnFragment.class);
    }

    @Test
    public void itShouldPushCompletingDeviceSetupScreenWhenNavigating() throws Exception {
        subject.navigateToCompletingDeviceSetupScreen();

        verifyFragmentPushed(EWSPressPlayAndFollowSetupFragment.class);
    }

    @Test
    public void itShouldPushConnectToDeviceWithPasswordScreenWhenNavigating() throws Exception {
        subject.navigateToConnectToDeviceWithPasswordScreen("deviceFriendlyName");

        verifyFragmentPushed(SetDeviceInfoFragment.class);
    }

    @Test
    public void itShouldPushPairingSuccessScreenWhenNavigating() throws Exception {
        subject.navigateToPairingSuccessScreen();

        verifyFragmentPushed(EWSWiFiPairedFragment.class);
    }

    @Test
    public void itShouldPushResetDeviceTroubleShootingScreenWhenNavigating() throws Exception {
        subject.navigateToResetDeviceTroubleShootingScreen();

        verifyFragmentPushed(ResetDeviceTroubleshootingFragment.class);
    }

    @Test
    public void itShouldPushConnectToWrongPhoneTroubleShootingScreenWhenNavigating() throws Exception {
        subject.navigateToConnectToWrongPhoneTroubleShootingScreen();

        verifyFragmentPushed(ConnectToWrongPhoneTroubleshootingFragment.class);
    }

    @Test
    public void itShouldPushSetupAccessPointModeScreenWhenNavigating() throws Exception {
        subject.navigateSetupAccessPointModeScreen();

        verifyFragmentPushed(SetupAccessPointModeTroubleshootingFragment.class);
    }

    @Test
    public void itShouldOnlyPopResetConnectionScreenWhenAlreadyPresentInStack() throws Exception {
        when(mockFragmentNavigator.popToFragment(anyString())).thenReturn(true);

        subject.navigateToResetConnectionTroubleShootingScreen();

        verify(mockFragmentNavigator).popToFragment(ResetConnectionTroubleshootingFragment.class.getCanonicalName());
        verify(mockFragmentNavigator, never()).push(any(Fragment.class), anyInt());
    }

    @Test
    public void itShouldPushResetConnectionScreenWhenNotPresentInStack() throws Exception {
        when(mockFragmentNavigator.popToFragment(anyString())).thenReturn(false);

        subject.navigateToResetConnectionTroubleShootingScreen();

        verifyFragmentPushed(ResetConnectionTroubleshootingFragment.class);
    }

    @Test
    public void itShouldOnlyPopPowerOnScreenWhenAlreadyPresentInStack() throws Exception {
        when(mockFragmentNavigator.popToFragment(anyString())).thenReturn(true);

        subject.navigateToDevicePoweredOnConfirmationScreen();

        verify(mockFragmentNavigator).popToFragment(EWSDevicePowerOnFragment.class.getCanonicalName());
        verify(mockFragmentNavigator, never()).push(any(Fragment.class), anyInt());
    }

    @Test
    public void itShouldPushPowerOnScreenWhenNotPresentInStack() throws Exception {
        when(mockFragmentNavigator.popToFragment(anyString())).thenReturn(false);

        subject.navigateToDevicePoweredOnConfirmationScreen();

        verifyFragmentPushed(EWSDevicePowerOnFragment.class);
    }

    @Test
    public void itShouldNavigateToWifiConnectionUnsuccessfulScreen() throws Exception {
        subject.navigateToWIFIConnectionUnsuccessfulTroubleShootingScreen("deviceName", "homeWifiSssid");

        verifyFragmentPushed(WifiConnectionUnsuccessfulFragment.class);
    }

    @Test
    public void itShouldNavigateToConnectingPhoneToHotspotWifiFragment() throws Exception {
        subject.navigateToConnectingPhoneToHotspotWifiScreen();

        verifyFragmentPushed(ConnectingPhoneToHotspotWifiFragment.class);
    }

    @Test
    public void itShouldNavigateToConnectingDeviceWithWifiScreenWithArgs() throws Exception {
        subject.navigateToConnectingDeviceWithWifiScreen("homeWifiSssid",
                "password", "deviceName", "deviceFriendlyName");

        verifyFragmentPushed(ConnectingDeviceWithWifiFragment.class);
    }

    @Test
    public void itShouldNavigateToConnectingDeviceWithWifiScreenWithBundle() throws Exception {
        Bundle data = mock(Bundle.class);
        when(data.containsKey(anyString())).thenReturn(true);
        when(data.getString(anyString())).thenReturn("dummyValue");

        subject.navigateToConnectingDeviceWithWifiScreen(data);

        verifyFragmentPushed(ConnectingDeviceWithWifiFragment.class);
    }

    @Test
    public void itShouldNavigateToEWSWiFiPairedScreen() throws Exception {
        subject.navigateToEWSWiFiPairedScreen();

        verifyFragmentPushed(EWSWiFiPairedFragment.class);
    }

    @Test
    public void itShouldNavigateToWrongWifiNetworkScreen() throws Exception {
        subject.navigateToWrongWifiNetworkScreen(new Bundle());

        verifyFragmentPushed(WrongWifiNetworkFragment.class);
    }

    @Test
    public void itShouldNavigateBack() throws Exception {
        subject.navigateBack();

        verify(mockFragmentNavigator).pop();
    }

    private void verifyFragmentPushed(@NonNull Class clazz) {
        ArgumentCaptor<Fragment> captor = ArgumentCaptor.forClass(Fragment.class);
        verify(mockFragmentNavigator).push(captor.capture(), eq(R.id.contentFrame));
        assertEquals(clazz, captor.getValue().getClass());
    }
}