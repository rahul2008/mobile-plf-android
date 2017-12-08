/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.confirmwifi;

import android.databinding.Observable;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConfirmWifiNetworkViewModelTest {

    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private Navigator navigatorMock;
    @Mock
    private ConfirmWifiNetworkViewModel.ViewCallback mockViewCallback;
    private ConfirmWifiNetworkViewModel subject;
    @Mock
    private Observable.OnPropertyChangedCallback callbackListenerMock;

    @Mock
    private BaseContentConfiguration mockBaseContentConfig;

    @Mock
    private StringProvider mockStringProvider;

    @Mock
    private EWSTagger mockEWSTagger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new ConfirmWifiNetworkViewModel(navigatorMock, wifiUtilMock, mockBaseContentConfig, mockStringProvider, mockEWSTagger);
        subject.setViewCallback(mockViewCallback);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(R.string.ews_device_name_default);
    }

    @Test
    public void itShouldGetHomeWiFiSSIDWhenAsked() throws Exception {
        String ssid = "brighteyes";

        when(wifiUtilMock.getConnectedWiFiSSID()).thenReturn(ssid);

        assertEquals(ssid, subject.getHomeWiFiSSID());
    }

    @Test
    public void itShouldShowDevicePowerOnScreenWhenClickedOnYesButtonIfWifiIsOn() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(true);
        subject.onYesButtonClicked();
        testConfirmNetworkEWSTagger();
        verify(navigatorMock).navigateToDevicePoweredOnConfirmationScreen();
    }

    @Test
    public void itShouldShowNetworkTroubleShootingScreenOnYesButtonIfWifiIsOff() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(false);
        subject.onYesButtonClicked();
        testChangeNetworkEWSTagger();
        verify(mockViewCallback).showTroubleshootHomeWifiDialog(mockBaseContentConfig, mockEWSTagger);
    }

    @Test
    public void itShouldShowNetworkTroubleShootingScreenOnNoButtonClicked() throws Exception {
        subject.onNoButtonClicked();
        testChangeNetworkEWSTagger();
        verify(mockViewCallback).showTroubleshootHomeWifiDialog(mockBaseContentConfig, mockEWSTagger);
    }

    @Test
    public void itShouldNotifyPropertyChangedWhenHomeWiFiSSIDIsCalled() throws Exception {
        subject.addOnPropertyChangedCallback(callbackListenerMock);
        subject.refresh();
        testChangeNetworkEWSTagger();
        verify(callbackListenerMock).onPropertyChanged(subject, BR.homeWiFiSSID);
    }

    @Test
    public void itShouldShowTroubleShootingScreenWhenNotConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(false);
        subject.refresh();
        testChangeNetworkEWSTagger();
        verify(mockViewCallback).showTroubleshootHomeWifiDialog(mockBaseContentConfig, mockEWSTagger);
    }

    @Test
    public void itShouldNotShowTroubleShootingScreenWhenConnectedToHomeWifi() throws Exception {
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(true);
        subject.refresh();
        verify(mockViewCallback, never()).showTroubleshootHomeWifiDialog(mockBaseContentConfig, mockEWSTagger);
    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception {
        subject.getTitle();
        verify(wifiUtilMock).getConnectedWiFiSSID();
    }

    @Test
    public void itShouldCallNoteWhenTitleIsCalledForViewModel() throws Exception {

        subject.getNote();
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_want_to_connect,
                mockBaseContentConfig.getDeviceName(), subject.getHomeWiFiSSID());

    }

    @Test
    public void itShouldVerifyNoteForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_confirm_connection_want_to_connect,
                mockBaseContentConfig.getDeviceName(), subject.getHomeWiFiSSID())).thenReturn("note");
        Assert.assertEquals("note", subject.getNote());
    }

    @Test
    public void itShouldGiveHelperText() throws Exception {
        subject.getHelperTitle();
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_tip_upper, mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldGiveHelperDescription() throws Exception {
        subject.getHelperDescription();
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_tip_lower, mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("confirmWifiNetwork");
    }

    @Test
    public void itShouldVerifyHelperDescription() throws Exception{
        subject.getHelperDescription();
        verify(mockStringProvider).getString(R.string.label_ews_confirm_connection_tip_lower,
                mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldVerifyValueForHomeWifiSSIdForEmptyString() throws Exception{
        String ssid = WiFiUtil.UNKNOWN_SSID;

        when(wifiUtilMock.getConnectedWiFiSSID()).thenReturn(ssid);
        assertEquals(subject.getHomeWiFiSSID(), "");
    }

    private void testConfirmNetworkEWSTagger() {
        verify(mockEWSTagger).trackActionSendData("specialEvents", "connectToExistingNetwork");
    }

    private void testChangeNetworkEWSTagger() {
        verify(mockEWSTagger).trackActionSendData("specialEvents", "changeNetworkToConnect");
    }
}