/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.Observable;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.confirmwifi.ConfirmWifiNetworkViewModel;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConfirmWifiNetworkViewModelTest {

    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private Navigator navigatorMock;
    @Mock
    private ConfirmWifiNetworkViewModel.ViewCallback mockViewCallback;
    private ConfirmWifiNetworkViewModel viewModel;
    @Mock
    private Observable.OnPropertyChangedCallback callbackListenerMock;

    @Mock
    private BaseContentConfiguration mockBaseContentConfig;

    @Mock
    private StringProvider mockStringProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        viewModel = new ConfirmWifiNetworkViewModel(navigatorMock, wifiUtilMock, mockBaseContentConfig, mockStringProvider);
        viewModel.setViewCallback(mockViewCallback);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(R.string.ews_device_name_default);
    }

    @Test
    public void itShouldGetHomeWiFiSSIDWhenAsked() throws Exception {
        String ssid = "brighteyes";

        when(wifiUtilMock.getConnectedWiFiSSID()).thenReturn(ssid);

        assertEquals(ssid, viewModel.getHomeWiFiSSID());
    }

    @Test
    public void itShouldShowDevicePowerOnScreenWhenClickedOnYesButton() throws Exception {
        viewModel.onYesButtonClicked();
        testConfirmNetworkEWSTagger();
        verify(navigatorMock).navigateToDevicePoweredOnConfirmationScreen();
    }

    @Test
    public void itShouldShowNetworkTroubleShootingScreenOnNoButtonClicked() throws Exception {
        viewModel.onNoButtonClicked();
        testChangeNetworkEWSTagger();
        verify(mockViewCallback).showTroubleshootHomeWifiDialog(mockBaseContentConfig);
    }

    @Test
    public void itShouldNotifyPropertyChangedWhenHomeWiFiSSIDIsCalled() throws Exception {
        viewModel.addOnPropertyChangedCallback(callbackListenerMock);
        viewModel.refresh();
        testChangeNetworkEWSTagger();
        verify(callbackListenerMock).onPropertyChanged(viewModel, BR.homeWiFiSSID);
    }

    private void testConfirmNetworkEWSTagger() {
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "connectToExistingNetwork");
    }

    private void testChangeNetworkEWSTagger() {
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "changeNetworkToConnect");
    }

    @Test
    public void itShouldShowTroubleShootingScreenWhenNotConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(false);
        viewModel.refresh();
        testChangeNetworkEWSTagger();
        verify(mockViewCallback).showTroubleshootHomeWifiDialog(mockBaseContentConfig);
    }

    @Test
    public void itShouldNotShowTroubleShootingScreenWhenConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(true);
        viewModel.refresh();
        verify(mockViewCallback, never()).showTroubleshootHomeWifiDialog(mockBaseContentConfig);
    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_confirm_connection_currently_connected,
                viewModel.getHomeWiFiSSID())).thenReturn("device name");
        Assert.assertEquals("device name", viewModel.getTitle());
    }

    @Test
    public void itShouldCallNoteWhenTitleIsCalledForViewModel() throws Exception {

        viewModel.getNote();
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_want_to_connect,
                mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID());

    }

    @Test
    public void itShouldVerifyNoteForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_confirm_connection_want_to_connect,
                mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID())).thenReturn("note");
        Assert.assertEquals("note", viewModel.getNote());
    }

}