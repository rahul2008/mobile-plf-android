/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.connectionestabilish.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

public class ConnectWithPasswordViewModelTest {

    private static final String PRODUCT = "product 001";
    @Mock
    private ApplianceSessionDetailsInfo sessionInfoMock;
    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private Navigator navigatorMock;
    @Mock
    private ConnectionEstablishDialogFragment dialogFragmentMock;
    @Mock
    private BaseContentConfiguration mockBaseContentConfig;
    @Mock
    private StringProvider mockStringProvider;
    private ConnectWithPasswordViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new ConnectWithPasswordViewModel(wifiUtilMock, sessionInfoMock, navigatorMock,
                dialogFragmentMock, mockBaseContentConfig, mockStringProvider);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(123435);
    }

    @Test
    public void itShouldCheckHomeWiFiSSIDShouldNotBeNull() throws Exception {
        when(wifiUtilMock.getHomeWiFiSSD()).thenReturn("BrightEyes");
        when(sessionInfoMock.getDeviceName()).thenReturn("Wakeup light");

        assertNotNull(viewModel.getHomeWiFiSSID());
    }

    @Test
    public void updateUpdatePasswordOnTextChanged() throws Exception {
        final String text = "abc";

        viewModel.onPasswordTextChanged(text, 0, 0, 1);

        assertEquals(text, viewModel.password.get());
    }

    @Test
    public void updateUpdateFriendlyDeviceNameOnTextChanged() throws Exception {
        final String text = "abc";

        viewModel.onDeviceNameTextChanged(text, 0, 0, 1);

        assertEquals(text, viewModel.deviceFriendlyName.get());
    }

    @Test
    public void itShouldHaveEmptyStringInPassword() {
        assertEquals(viewModel.password.get(), "");
    }

    @Test
    public void itShouldSendConnectionTagsWhenWeRevisitThisPageAgain() throws Exception {
        viewModel.onConnectButtonClicked();

        verify(navigatorMock)
                .navigateToConnectingDeviceWithWifiScreen(anyString(), anyString(), anyString(),
                        anyString());
    }

    @Test
    public void itShouldSetTheDeviceFriendlyName() {
        final String text = "abc";
        viewModel.setDeviceFriendlyName(text);
        assertEquals(text, viewModel.deviceFriendlyName.get());
    }

    @Test
    public void itShouldVerifyTitleForViewModel() throws Exception {
        viewModel.getTitle(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID());
    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID())).thenReturn("device name");
        Assert.assertEquals("device name", viewModel.getTitle(mockBaseContentConfig));
    }

    @Test
    public void itShouldVerifyNoteForScreen() throws Exception {
        viewModel.getNote(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldVerifyNoteForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName())).thenReturn("device name");
        Assert.assertEquals("device name", viewModel.getNote(mockBaseContentConfig));
    }


    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        verifyStatic();
        EWSTagger.trackPage("connectWithPassword");
    }
}