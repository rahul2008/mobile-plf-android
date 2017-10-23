/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.Observable;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

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

    private EWSHomeWifiDisplayViewModel viewModel;
    @Mock
    private Observable.OnPropertyChangedCallback callbackListenerMock;

    @Mock
    private BaseContentConfiguration mockBaseContentConfig;

    @Mock
    private StringProvider mockStringProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new EWSHomeWifiDisplayViewModel(navigatorMock, wifiUtilMock, mockBaseContentConfig, mockStringProvider);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(R.string.ews_device_name_default);
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

        verify(navigatorMock).navigateToWifiTroubleShootingScreen();
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

        verify(navigatorMock).navigateToWifiTroubleShootingScreen();
    }

    @Test
    public void itShouldNotShowTroubleShootingScreenWhenConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(true);

        viewModel.refresh();

        verify(navigatorMock, never()).navigateToWifiTroubleShootingScreen();
    }

    @Test
    public void itShouldCallGetStringWhenTitleIsCalledForViewModel() throws Exception {
        viewModel.getTitle(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_currently_connected,
                mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID());

    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception{
        when(mockStringProvider.getString(R.string.label_ews_confirm_connection_currently_connected,
                mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID())).thenReturn("device name");
        Assert.assertEquals("device name", viewModel.getTitle(mockBaseContentConfig));
    }

    @Test
    public void itShouldCallNoteWhenTitleIsCalledForViewModel() throws Exception {

        viewModel.getNote(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_want_to_connect,
                mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID());

    }

    @Test
    public void itShouldVerifyNoteForViewMatches() throws Exception{
        when(mockStringProvider.getString(R.string.label_ews_confirm_connection_want_to_connect,
                mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID())).thenReturn("note");
        Assert.assertEquals("note", viewModel.getNote(mockBaseContentConfig));
    }

}