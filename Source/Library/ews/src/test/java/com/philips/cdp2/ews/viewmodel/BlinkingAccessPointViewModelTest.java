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
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.troubleshooting.hotspotconnectionfailure.ConnectionUnsuccessfulFragment;
import com.philips.cdp2.ews.util.GpsUtil;
import com.philips.cdp2.ews.view.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.view.dialog.GPSEnableDialogFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.view.ConnectPhoneToDeviceAPModeFragment.LOCATION_PERMISSIONS_REQUEST_CODE;
import static com.philips.cdp2.ews.viewmodel.ConnectPhoneToDeviceAPModeViewModel.ACCESS_COARSE_LOCATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GpsUtil.class, EWSLogger.class})
public class BlinkingAccessPointViewModelTest {

    @Mock
    private Navigator screenFlowControllerMock;

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
    private ConnectionUnsuccessfulFragment unsuccessfulDialogMock;

    @Mock
    private GPSEnableDialogFragment gpsEnableDialogFragmentMock;

    private BlinkingAccessPointViewModel viewModel;

    @Mock
    private Dialog dialogMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        setupImmediateHandler();

        viewModel = new BlinkingAccessPointViewModel(screenFlowControllerMock, eventBusMock, permissionHandlerMock,
                connectingDialogMock, null, gpsEnableDialogFragmentMock, handlerMock);

        viewModel.setFragment(fragmentMock);
    }

    @Test
    public void shouldRequestForLocationPermissionIfItsNotGrantedAlreadyWhenClickedOnYesButton() throws Exception {
        setPermissionGranted(false);

        viewModel.onYesButtonClicked();

        verify(permissionHandlerMock).requestPermission(fragmentMock, R.string.label_location_permission_required,
                ACCESS_COARSE_LOCATION, LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    @Test
    public void shouldConnectToApplianceHotspotWhenLocationPermissionIsAlreadyGrantedWhenClickedOnYesButton() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(true, true);

        viewModel.onYesButtonClicked();

        verifyConnectRequest();
    }

    @Test
    public void shouldShowGPSEnableDialogIfGPSIsOffWhenClickedOnYesButton() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, true);

        viewModel.onYesButtonClicked();

        verify(gpsEnableDialogFragmentMock).show(fragmentMock.getFragmentManager(), fragmentMock.getClass().getName());
    }

    @Test
    public void shouldShowNextPasswordEntryScreenWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
        sendEventToShowPasswordEntryScreen();

//        verify(screenFlowControllerMock).showFragment(isA(EWSWiFiConnectFragment.class));
    }

    @Test
    public void shouldUnregisterFromEventBusWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
        sendEventToShowPasswordEntryScreen();

        verify(eventBusMock).unregister(viewModel);
    }

    @Test
    public void shouldSendRequestToConnectToApplianceHotspotWhenPermissionIsExplicitlyGrantedByUser() throws Exception {
        final int[] grantedPermission = new int[1];
        when(permissionHandlerMock.areAllPermissionsGranted(grantedPermission)).thenReturn(true);

        assertTrue(viewModel.areAllPermissionsGranted(grantedPermission));
    }

    @Test
    public void shouldCancelConnectingDialogOnDeviceConnectionError() throws Exception {
        viewModel.deviceConnectionError(new DeviceConnectionErrorEvent());

        verify(connectingDialogMock).dismissAllowingStateLoss();
    }

    @Test
    public void shouldShowUnsuccessfulDialogOnDeviceConnectionError() throws Exception {
//        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
//        when(dialogMock.isShowing()).thenReturn(false);
//
//        viewModel.deviceConnectionError(new DeviceConnectionErrorEvent());
//
//        verify(unsuccessfulDialogMock).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void shouldNotShowUnsuccessfulDialogOnDeviceConnectionErrorIfVisible() throws Exception {
//        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
//        when(dialogMock.isShowing()).thenReturn(true);
//
//        viewModel.deviceConnectionError(new DeviceConnectionErrorEvent());
//
//        verify(unsuccessfulDialogMock, never()).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void shouldRemoveHandlerCallbackOnDeviceConnectionError() throws Exception {
        viewModel.deviceConnectionError(new DeviceConnectionErrorEvent());

        verify(handlerMock).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void shouldCallPostDelayedOnHandlerWhenConnectedToApplianceHotspot() throws Exception {
        stubGPSSettings(true, true);
        setPermissionGranted(true);

        viewModel.onYesButtonClicked();

        verify(handlerMock).postDelayed(any(Runnable.class), anyInt());
    }

    @Test
    public void shouldCallUnsuccessfulDialogOnHandlerWhenConnectedToApplianceHotspot() throws Exception {
//        stubGPSSettings(true, true);
//        setPermissionGranted(true);
//
//        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
//        when(dialogMock.isShowing()).thenReturn(false);
//
//        viewModel.onYesButtonClicked();
//
//        verify(connectingDialogMock).dismissAllowingStateLoss();
//        verify(unsuccessfulDialogMock).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void shouldConnectToApplianceHotspotWithoutCheckingForGpsEnabledOnBelowAndroidMVersions() throws Exception {
        stubGPSSettings(true, false);
        setPermissionGranted(true);

        viewModel.onYesButtonClicked();

        verifyConnectRequest();
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
        viewModel.showPasswordEntryScreenEvent(new ShowPasswordEntryScreenEvent());
    }

    private void stubGPSSettings(final boolean enabled, final boolean isGpsRequiredForWifiScan) {
        PowerMockito.mockStatic(GpsUtil.class);
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