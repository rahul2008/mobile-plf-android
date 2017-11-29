package com.philips.cdp2.ews.navigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NavigatorTest {

    @InjectMocks
    private Navigator subject;

    @Mock
    private FragmentNavigator mockFragmentNavigator;

    private ArgumentCaptor<Fragment> captor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        captor = ArgumentCaptor.forClass(Fragment.class);
    }

    @Test
    public void itShouldGetFragmentNavigatorWhichIsGiven() throws Exception{
        assertSame(subject.getFragmentNavigator(),mockFragmentNavigator);
    }

    @Test
    public void itShouldPushGettingStartingFragmentWhenNavigating() throws Exception {
        subject.navigateToGettingStartedScreen();

        verifyFragmentPushed(StartConnectWithDeviceFragment.class);
    }

    @Test
    public void itShouldPushHomeNetworkConfirmationScreenWhenNavigating() throws Exception {
        subject.navigateToHomeNetworkConfirmationScreen();

        verifyFragmentPushed(ConfirmWifiNetworkFragment.class);
    }

    @Test
    public void itShouldPushDevicePoweredOnConfirmationScreenWhenNavigating() throws Exception {
        subject.navigateToDevicePoweredOnConfirmationScreen();

        verifyFragmentPushed(FirstSetupStepsFragment.class);
    }

    @Test
    public void itShouldPushCompletingDeviceSetupScreenWhenNavigating() throws Exception {
        subject.navigateToCompletingDeviceSetupScreen();

        verifyFragmentPushed(SecondSetupStepsFragment.class);
    }

    @Test
    public void itShouldPushConnectToDeviceWithPasswordScreenWhenNavigating() throws Exception {
        subject.navigateToConnectToDeviceWithPasswordScreen("deviceFriendlyName");

        verifyFragmentPushedForNoPopBackStack(ConnectWithPasswordFragment.class);
    }

    @Test
    public void itShouldPushPairingSuccessScreenWhenNavigating() throws Exception {
        subject.navigateToPairingSuccessScreen();

        verifyFragmentPushed(ConnectionSuccessfulFragment.class);
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
    public void itShouldPushResetConnectionScreenWhenNotPresentInStack() throws Exception {
        when(mockFragmentNavigator.popToFragment(anyString())).thenReturn(false);

        subject.navigateToResetConnectionTroubleShootingScreen();

        verifyFragmentPushed(ResetConnectionTroubleshootingFragment.class);
    }

    @Test
    public void itShouldPushPowerOnScreenWhenNotPresentInStack() throws Exception {
        when(mockFragmentNavigator.popToFragment(anyString())).thenReturn(false);

        subject.navigateToDevicePoweredOnConfirmationScreen();

        verifyFragmentPushed(FirstSetupStepsFragment.class);
    }

    @Test
    public void itShouldNavigateToWifiConnectionUnsuccessfulScreen() throws Exception {
        subject.navigateToWIFIConnectionUnsuccessfulTroubleShootingScreen("deviceName", "homeWifiSssid");

        verifyFragmentPushedForNoPopBackStack(WifiConnectionUnsuccessfulFragment.class);
    }

    @Test
    public void itShouldNavigateToConnectingPhoneToHotspotWifiFragment() throws Exception {
        subject.navigateToConnectingPhoneToHotspotWifiScreen();

        verifyFragmentPushedForNoPopBackStack(ConnectingWithDeviceFragment.class);
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

        verifyFragmentPushedForNoPopBackStack(ConnectionSuccessfulFragment.class);
    }

    @Test
    public void itShouldNavigateToWrongWifiNetworkScreen() throws Exception {
        subject.navigateToWrongWifiNetworkScreen(new Bundle());

        verifyFragmentPushed(WrongWifiNetworkFragment.class);
    }

    @Test
    public void itShouldVerifyFragmentNavigatorPopIsCalledWhenNavigateBack() throws Exception {
        subject.navigateBack();

        verify(mockFragmentNavigator).pop();
    }

    private void verifyFragmentPushed(@NonNull Class fragmentClass) {
        verify(mockFragmentNavigator).push(captor.capture(), anyInt(),anyBoolean(),eq(true));
        assertEquals(fragmentClass, captor.getValue().getClass());
    }

    private void verifyFragmentPushedForNoPopBackStack(@NonNull Class fragmentClass) {
        verify(mockFragmentNavigator).push(captor.capture(), anyInt(),anyBoolean(),eq(false));
        assertEquals(fragmentClass, captor.getValue().getClass());
    }
}