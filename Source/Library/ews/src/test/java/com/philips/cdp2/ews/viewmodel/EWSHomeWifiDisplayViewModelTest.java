/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.Observable;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSHomeWifiDisplayViewModelTest {

    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private Navigator navigatorMock;
    @Mock
    private EWSHomeWifiDisplayViewModel.ViewCallback mockViewCallback;
    private EWSHomeWifiDisplayViewModel viewModel;
    @Mock
    private Observable.OnPropertyChangedCallback callbackListenerMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new EWSHomeWifiDisplayViewModel(navigatorMock, wifiUtilMock);
        viewModel.setViewCallback(mockViewCallback);
    }

    @Test
    public void shouldGetHomeWiFiSSIDWhenAsked() throws Exception {
        String ssid = "brighteyes";

        when(wifiUtilMock.getConnectedWiFiSSID()).thenReturn(ssid);

        assertEquals(ssid, viewModel.getHomeWiFiSSID());
    }

    @Test
    public void shouldShowDevicePowerOnScreenWhenClickedOnYesButton() throws Exception {
        viewModel.onYesButtonClicked();

        verify(navigatorMock).navigateToDevicePoweredOnConfirmationScreen();
    }

    @Test
    public void shouldShowNetworkTroubleShootingScreenOnNoButtonClicked() throws Exception {
        viewModel.onNoButtonClicked();

        verify(mockViewCallback).showTroubleshootHomeWifiDialog();
    }

    @Test
    public void shouldNotifyPropertyChangedWhenHomeWiFiSSIDIsCalled() throws Exception {
        viewModel.addOnPropertyChangedCallback(callbackListenerMock);
        viewModel.refresh();

        verify(callbackListenerMock).onPropertyChanged(viewModel, BR.homeWiFiSSID);
    }

    @Test
    public void itShouldShowTroubleShootingScreenWhenNotConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(false);

        viewModel.refresh();

        verify(mockViewCallback).showTroubleshootHomeWifiDialog();
    }

    @Test
    public void itShouldNotShowTroubleShootingScreenWhenConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(true);

        viewModel.refresh();

        verify(mockViewCallback, never()).showTroubleshootHomeWifiDialog();
    }
}