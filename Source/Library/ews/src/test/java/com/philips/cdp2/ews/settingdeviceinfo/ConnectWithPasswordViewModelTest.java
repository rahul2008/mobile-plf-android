/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConnectWithPasswordViewModelTest {

    private static final String PRODUCT = "product 001";
    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private Navigator navigatorMock;
    @Mock
    private BaseContentConfiguration mockBaseContentConfig;
    @Mock
    private StringProvider mockStringProvider;
    @Mock
    private View mockView;
    @Mock
    private InputMethodManager mockInputMethodManager;

    private ConnectWithPasswordViewModel subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new ConnectWithPasswordViewModel(wifiUtilMock, navigatorMock,
                 mockBaseContentConfig, mockStringProvider);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(123435);
    }

    @Test
    public void itShouldCheckHomeWiFiSSIDShouldNotBeNull() throws Exception {
        when(wifiUtilMock.getHomeWiFiSSD()).thenReturn("BrightEyes");

        assertNotNull(subject.getHomeWiFiSSID());
    }

    @Test
    public void updateUpdatePasswordOnTextChanged() throws Exception {
        final String text = "abc";

        subject.onPasswordTextChanged(text, 0, 0, 1);

        assertEquals(text, subject.password.get());
    }

    @Test
    public void updateUpdateFriendlyDeviceNameOnTextChanged() throws Exception {
        final String text = "abc";

        subject.onDeviceNameTextChanged(text, 0, 0, 1);

        assertEquals(text, subject.deviceFriendlyName.get());
    }

    @Test
    public void itShouldHaveEmptyStringInPassword() {
        assertEquals(subject.password.get(), "");
    }

    @Test
    public void itShouldSendConnectionTagsWhenWeRevisitThisPageAgain() throws Exception {
        subject.onConnectButtonClicked();

        verify(navigatorMock)
                .navigateToConnectingDeviceWithWifiScreen(anyString(), anyString(), anyString(),
                        anyString());
    }

    @Test
    public void itShouldSetTheDeviceFriendlyName() {
        final String text = "abc";
        subject.setDeviceFriendlyName(text);
        assertEquals(text, subject.deviceFriendlyName.get());
    }

    @Test
    public void itShouldVerifyTitleForViewModel() throws Exception {
        subject.getTitle(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), subject.getHomeWiFiSSID());
    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), subject.getHomeWiFiSSID())).thenReturn("device name");
        Assert.assertEquals("device name", subject.getTitle(mockBaseContentConfig));
    }

    @Test
    public void itShouldVerifyNoteForScreen() throws Exception {
        subject.getNote(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldVerifyNoteForViewMatches() throws Exception {
        when(mockStringProvider.getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName())).thenReturn("device name");
        Assert.assertEquals("device name", subject.getNote(mockBaseContentConfig));
    }


    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        subject.trackPageName();
        verifyStatic();
        EWSTagger.trackPage("connectWithPassword");
    }

    @Test
    public void itShouldVerifyOnPasswordFocusChange() throws Exception {
        subject.onPasswordFocusChange(mockView, mockInputMethodManager, false);
        verify(mockInputMethodManager).hideSoftInputFromWindow(mockView.getWindowToken(), 0);
    }

    @Test
    public void itShouldVerifyOnDeviceNameFocusChange() throws Exception {
        subject.onDeviceNameFocusChange(mockView, mockInputMethodManager, false);
        verify(mockInputMethodManager).hideSoftInputFromWindow(mockView.getWindowToken(), 0);
    }
}