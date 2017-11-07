/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.app.Dialog;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.communication.events.DeviceConnectionErrorEvent;
import com.philips.cdp2.ews.communication.events.NetworkConnectEvent;
import com.philips.cdp2.ews.communication.events.ShowPasswordEntryScreenEvent;
import com.philips.cdp2.ews.connectionestabilish.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.dialog.GPSEnableDialogFragment;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.setupsteps.SecondSetupStepsViewModel;
import com.philips.cdp2.ews.util.GpsUtil;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.setupsteps.ConnectPhoneToDeviceAPModeViewModel.ACCESS_COARSE_LOCATION;
import static com.philips.cdp2.ews.setupsteps.SecondSetupStepsFragment.LOCATION_PERMISSIONS_REQUEST_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GpsUtil.class, EWSLogger.class, EWSTagger.class})
public class SecondSetupStepsViewModelTest {

    @Mock
    private Navigator navigatorMock;

    @Mock
    private EventBus eventBusMock;

    @Mock
    private PermissionHandler permissionHandlerMock;

    @Mock
    private Fragment fragmentMock;

    @Mock
    private ConnectionEstablishDialogFragment connectingDialogMock;

    @Mock
    private Handler handlerMock;

    @Mock
    private GPSEnableDialogFragment gpsEnableDialogFragmentMock;

    private SecondSetupStepsViewModel subject;

    @Mock
    private Dialog dialogMock;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSLogger.class);
        mockStatic(EWSTagger.class);
        setupImmediateHandler();
        subject = new SecondSetupStepsViewModel(navigatorMock, eventBusMock, permissionHandlerMock,
                connectingDialogMock, null, gpsEnableDialogFragmentMock, handlerMock);

        subject.setFragment(fragmentMock);
    }

    @Test
    public void itShouldRequestForLocationPermissionIfItsNotGrantedAlreadyWhenONextButtonClicked() throws Exception {
        setPermissionGranted(false);
        subject.onNextButtonClicked();
        verify(permissionHandlerMock).requestPermission(fragmentMock, R.string.label_location_permission_required,
                ACCESS_COARSE_LOCATION, LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    @Test
    public void itShouldConnectToApplianceHotspotWhenLocationPermissionIsAlreadyGrantedWhenONextButtonClicked() throws Exception {
//        setPermissionGranted(true);
//        stubGPSSettings(true, true);
//
//        subject.onNextButtonClicked();
//
//        verifyConnectRequest();
    }

    @Test
    public void itShouldShowGPSEnableDialogIfGPSIsOffWhenONextButtonClicked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, true);
        subject.onNextButtonClicked();
        verify(gpsEnableDialogFragmentMock).show(fragmentMock.getFragmentManager(), fragmentMock.getClass().getName());
    }

    @Test
    public void itShouldShowNextPasswordEntryScreenWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
        sendEventToShowPasswordEntryScreen();

//        verify(navigatorMock).showFragment(isA(ConnectWithPasswordFragment.class));
    }


    @Test
    public void itShouldDismissDialogWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
//        sendEventToShowPasswordEntryScreen();
//
//        verify(connectingDialogMock).dismissAllowingStateLoss();
    }

    @Test
    public void itShouldSendRequestToConnectToApplianceHotspotWhenPermissionIsExplicitlyGrantedByUser() throws Exception {
        final int[] grantedPermission = new int[1];
        when(permissionHandlerMock.areAllPermissionsGranted(grantedPermission)).thenReturn(true);

        assertTrue(subject.areAllPermissionsGranted(grantedPermission));
    }

    @Test
    public void itShouldCancelConnectingDialogOnDeviceConnectionError() throws Exception {
        subject.deviceConnectionError(new DeviceConnectionErrorEvent());

        verify(connectingDialogMock).dismissAllowingStateLoss();
    }

    @Test
    public void itShouldShowUnsuccessfulDialogOnDeviceConnectionError() throws Exception {
//        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
//        when(dialogMock.isShowing()).thenReturn(false);
//
//        subject.deviceConnectionError(new DeviceConnectionErrorEvent());
//
//        verify(unsuccessfulDialogMock).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void itShouldNotShowUnsuccessfulDialogOnDeviceConnectionErrorIfVisible() throws Exception {
//        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
//        when(dialogMock.isShowing()).thenReturn(true);
//
//        subject.deviceConnectionError(new DeviceConnectionErrorEvent());
//
//        verify(unsuccessfulDialogMock, never()).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void itShouldRemoveHandlerCallbackOnDeviceConnectionError() throws Exception {
        subject.deviceConnectionError(new DeviceConnectionErrorEvent());

        verify(handlerMock).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void itShouldCallPostDelayedOnHandlerWhenConnectedToApplianceHotspot() throws Exception {
//        stubGPSSettings(true, true);
//        setPermissionGranted(true);
//
//        subject.onNextButtonClicked();
//
//        verify(handlerMock).postDelayed(any(Runnable.class), anyInt());
    }

    @Test
    public void itShouldCallUnsuccessfulDialogOnHandlerWhenConnectedToApplianceHotspot() throws Exception {
//        stubGPSSettings(true, true);
//        setPermissionGranted(true);
//
//        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
//        when(dialogMock.isShowing()).thenReturn(false);
//
//        subject.onNextButtonClicked();
//
//        verify(connectingDialogMock).dismissAllowingStateLoss();
//        verify(unsuccessfulDialogMock).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void itShouldConnectToApplianceHotspotWithoutCheckingForGpsEnabledOnBelowAndroidMVersions() throws Exception {
//        stubGPSSettings(true, false);
//        setPermissionGranted(true);
//
//        subject.onNextButtonClicked();
//
//        verifyConnectRequest();
    }

    @Test
    public void itShouldShowChooseCurrentStateScreenWhenNoButtonIsClicked() throws Exception {
        subject.onNoButtonClicked();
        verify(navigatorMock).navigateToResetConnectionTroubleShootingScreen();
    }

    @Test
    public void itShouldSendWifiBlinkingActionTagOnNextButtonClick() throws Exception{
        subject.onNextButtonClicked();
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "wifiBlinking");
    }

    @Test
    public void itShouldSendWifiNotBlinkingActionTagOnNoButtonClick() throws Exception{
        subject.onNoButtonClicked();
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "wifiNotBlinking");
    }

    private void setPermissionGranted(final boolean permissionGranted) {
        when(permissionHandlerMock.hasPermission(fragmentMock.getContext(), ACCESS_COARSE_LOCATION)).
                thenReturn(permissionGranted);
    }

    private void verifyConnectRequest() {
        ArgumentCaptor<NetworkConnectEvent> argumentCaptor = ArgumentCaptor.forClass(NetworkConnectEvent.class);
        verify(eventBusMock).post(argumentCaptor.capture());

        NetworkConnectEvent networkConnectEvent = argumentCaptor.getValue();

        verify(eventBusMock).post(isA(NetworkConnectEvent.class));
        verify(connectingDialogMock).show(fragmentMock.getFragmentManager(), fragmentMock.getClass().getName());
        assertEquals(NetworkType.DEVICE_HOTSPOT, networkConnectEvent.getNetworkType());
        assertEquals(WiFiUtil.DEVICE_SSID, networkConnectEvent.getNetworkSSID());
    }

    private void sendEventToShowPasswordEntryScreen() {
        subject.showPasswordEntryScreenEvent(new ShowPasswordEntryScreenEvent());
    }

    private void stubGPSSettings(final boolean enabled, final boolean isGpsRequiredForWifiScan) {
        mockStatic(GpsUtil.class);
        when(GpsUtil.isGPSRequiredForWifiScan()).thenReturn(isGpsRequiredForWifiScan);
        when(GpsUtil.isGPSEnabled(fragmentMock.getContext())).thenReturn(enabled);
    }

    private void setupImmediateHandler() {
        // Run any post delayed task immediately.
        when(handlerMock.postDelayed(any(Runnable.class), anyLong())).thenAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                        runnable.run();
                        return null;
                    }
                }
        );
    }
}